package cz.martinvedra.triagebe1.service

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClientBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import cz.martinvedra.triagebe1.api.dto.ExchangeRateDto
import cz.martinvedra.triagebe1.config.properties.AppConfigurationProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Service
class StorageService(
    private val objectMapper: ObjectMapper,
    private val properties: AppConfigurationProperties,
    private val containerClient: BlobContainerClient
) {

    private val log = LoggerFactory.getLogger(StorageService::class.java)

//    @Value("\${app.storage.local-path}")
//    private lateinit var localPath: String

//    @Value("\${app.storage.azure-connection-string}")
//    private lateinit var connectionString: String

//    @Value("\${app.storage.container-name}")
//    private lateinit var containerName: String

    fun saveRatesToFile(rates: List<ExchangeRateDto>) {
        try {
            val path = Paths.get(properties.storage.localPath)
            Files.createDirectories(path.parent)
            val jsonString = objectMapper.writeValueAsString(rates)
//            File(localPath).writeText(jsonString)
//            Files.write(path, jsonString.toByteArray(), StandardOpenOption.WRITE)
//Files.writeString(path, jsonString, StandardOpenOption.CREATE)
//            Files.write(path, jsonString.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND)
            Files.writeString(path, jsonString, StandardCharsets.UTF_8)
            log.info("Saved exchange rates to ${properties.storage.localPath}")
        } catch (e: IOException) {
            log.error("Failed to save exchange rates to $properties.storage.localPath", e)
        }
    }

    fun uploadToAzure(rateDto: List<ExchangeRateDto>) {
        try {

            val jsonString = objectMapper.writeValueAsString(rateDto)
            val data = jsonString.toByteArray(Charsets.UTF_8)

            val blobClient = containerClient.getBlobClient("latest-rates.json")

            ByteArrayInputStream(data).use { inputStream ->
                blobClient.upload(inputStream, data.size.toLong(), true)
            }

            log.info("Saved exchange rates to Azure Storage")
        } catch (e: Exception) {
            log.error("Failed to upload exchange rates to Azure Storage", e)
        }

    }
}