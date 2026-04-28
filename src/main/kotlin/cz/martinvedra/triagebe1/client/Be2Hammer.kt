package cz.martinvedra.triagebe1.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class Be2Hammer(
    private val restClient: RestClient = RestClient.create()
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun fireRequests(count: Int) {
        log.info("Starting to fire $count requests to BE2...")

        // Tady použijeme Coroutines pro masivní paralelní zátěž
        runBlocking {
            repeat(count) { i ->
                launch(Dispatchers.IO) {
                    try {
                        val response = restClient.get()
                            .uri("http://localhost:8081/api/rates") // Adresa BE2
                            .retrieve()
                            .toBodilessEntity()

                        log.info("Request $i: Status ${response.statusCode}")
                    } catch (e: Exception) {
                        log.error("Request $i failed: ${e.message}")
                    }
                }
            }
        }
    }
}