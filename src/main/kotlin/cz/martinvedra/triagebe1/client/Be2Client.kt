package cz.martinvedra.triagebe1.client

import cz.martinvedra.triagebe1.api.dto.ExchangeRateDto
import cz.martinvedra.triagebe1.config.properties.AppConfigurationProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.util.Collections
import kotlin.collections.toList

@Component
class Be2Client(
    private val properties: AppConfigurationProperties
) {

    private val log = LoggerFactory.getLogger(javaClass)

    private val restClient: RestClient by lazy {
        log.info("Initializing RestClient for CSAS API")
        RestClient.builder()
            .baseUrl(properties.be2.baseUrl)
            .build()
    }

    fun callBe2getRates(): List<ExchangeRateDto> {
        log.debug("Fetching exchange rates from CSAS API")
        return try {
            restClient.get()
                .uri("/api/be2")
                .retrieve()
                .body<Array<ExchangeRateDto>>()
                ?.toList() ?: Collections.emptyList()
        } catch (e: Exception) {
            log.error("Failed to fetch exchange rates: ${e.message}", e)
            Collections.emptyList()
        }
    }
}