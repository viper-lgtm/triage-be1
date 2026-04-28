package cz.martinvedra.triagebe1.api

import cz.martinvedra.triagebe1.api.dto.ExchangeRateDto
import cz.martinvedra.triagebe1.service.Be1Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/be1")
class Be1Controller(
    private val service: Be1Service
) {

    @GetMapping
    fun be2ExchangeRate(): List<ExchangeRateDto> {
        return service.getExchangeRate()
    }
}