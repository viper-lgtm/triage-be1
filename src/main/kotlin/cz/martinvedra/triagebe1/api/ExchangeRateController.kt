package cz.martinvedra.triagebe1.api

import cz.martinvedra.triagebe1.api.dto.ExchangeRateDto
import cz.martinvedra.triagebe1.service.ExchangeRateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/be1")
class ExchangeRateController(
    private val exchangeRateService: ExchangeRateService
) {

    @GetMapping
    fun be2ExchangeRate(): List<ExchangeRateDto> {
        return exchangeRateService.getRates("default")
    }

    // Explicit cache invalidation endpoint
    @DeleteMapping("/cache/{feedId}")
    fun invalidateCache(@PathVariable feedId: String): ResponseEntity<Void> {
        return try {
            exchangeRateService.invalidateCache(feedId)
            ResponseEntity.noContent().build()
        } catch (ex: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @PostMapping("/cache/{feedId}")
    fun saveToCache(@PathVariable feedId: String, @RequestBody rates: List<ExchangeRateDto>): ResponseEntity<Void> {
        return try {
            exchangeRateService.saveOrUpdateRates(feedId, rates)
            ResponseEntity.accepted().build()
        } catch (ex: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
}