package cz.martinvedra.triagebe1.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import java.time.Duration

@Service
class RedisCacheService(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) {

    fun get(key: String): String? = redisTemplate.opsForValue().get(key) as? String

    fun cacheValue(key: String, value: Any, ttl: Duration) {
        val json = if (value is String) value else objectMapper.writeValueAsString(value)
        redisTemplate.opsForValue().set(key, json, ttl)
    }

    fun delete(key: String) {
        redisTemplate.delete(key)
    }
}