package cz.martinvedra.triagebe1.service

import cz.martinvedra.triagebe1.api.dto.ExchangeRateDto
import cz.martinvedra.triagebe1.client.Be2Client
import org.springframework.stereotype.Service

@Service
class Be1Service(
    private val client: Be2Client
) {

    fun getExchangeRate(): List<ExchangeRateDto> {
            return client.callBe2getRates()
    }
}