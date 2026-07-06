package cz.martinvedra.triagebe1.service

import cz.martinvedra.triagebe1.api.dto.ExchangeRateDto
import cz.martinvedra.triagebe1.client.Be2Client
import cz.martinvedra.triagebe1.model.ExchangeRateSnapshot
import cz.martinvedra.triagebe1.repository.ExchangeRateSnapshotRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.Duration

@Service
class ExchangeRateService(
    private val redisCacheService: RedisCacheService,
    private val objectMapper: ObjectMapper,
    private val be2Client: Be2Client,
    private val snapshotRepository: ExchangeRateSnapshotRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private val ttl: Duration = Duration.ofMinutes(5)

    private fun keyFor(feedId: String) = "triage:rates:$feedId"
    private fun hashKeyFor(feedId: String) = "${keyFor(feedId)}:hash"

    fun saveOrUpdateRates(feedId: String, payload: Any) {
        val key = keyFor(feedId)
        val hashKey = hashKeyFor(feedId)

        val payloadJson = if (payload is String) payload else objectMapper.writeValueAsString(payload)
        val payloadHash = sha256Hex(payloadJson)

        val cachedHash = redisCacheService.get(hashKey)
        if (cachedHash != null && cachedHash == payloadHash) {
            log.info("Payload for {} unchanged — skipping DB and cache update", feedId)
            return
        }

        // Persist snapshot
        val snapshot = ExchangeRateSnapshot(
            feedId = feedId,
            payload = payloadJson,
            payloadHash = payloadHash
        )
        snapshotRepository.save(snapshot)

        // Update cache (value + hash)
        try {
            redisCacheService.cacheValue(key, payloadJson, ttl)
            redisCacheService.cacheValue(hashKey, payloadHash, ttl)
        } catch (ex: Exception) {
            log.warn("Failed to write to Redis cache: {}", ex.message)
        }
    }

    fun getRates(feedId: String): List<ExchangeRateDto> {
        val key = keyFor(feedId)

        // Try cache first
        val cached = try {
            redisCacheService.get(key)
        } catch (ex: Exception) {
            log.warn("Failed to read from Redis: {}", ex.message)
            null
        }

        if (cached != null) {
            return try {
                objectMapper.readValue(cached, Array<ExchangeRateDto>::class.java).toList()
            } catch (ex: Exception) {
                log.warn("Failed to deserialize cached payload: {}", ex.message)
                emptyList()
            }
        }

        // Cache miss — call external API
        val fetched = be2Client.callBe2getRates()

        // Persist + cache
        try {
            val json = objectMapper.writeValueAsString(fetched)
            val hash = sha256Hex(json)
            val snapshot = ExchangeRateSnapshot(feedId = feedId, payload = json, payloadHash = hash)
            snapshotRepository.save(snapshot)
            redisCacheService.cacheValue(key, json, ttl)
            redisCacheService.cacheValue(hashKeyFor(feedId), hash, ttl)
        } catch (ex: Exception) {
            log.warn("Failed to persist/cache fetched rates: {}", ex.message)
        }

        return fetched
    }

    fun invalidateCache(feedId: String) {
        try {
            redisCacheService.delete(keyFor(feedId))
            redisCacheService.delete(hashKeyFor(feedId))
            log.info("Invalidated cache for feed {}", feedId)
        } catch (ex: Exception) {
            log.warn("Failed to invalidate cache for {}: {}", feedId, ex.message)
        }
    }

    private fun sha256Hex(text: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(text.toByteArray(StandardCharsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }
}