package cz.martinvedra.triagebe1.config.azure

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClientBuilder
import cz.martinvedra.triagebe1.config.properties.AppConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AzureStorageConfig {

    @Bean
    fun blobContainerClient(properties: AppConfigurationProperties): BlobContainerClient {
        val blobServiceClient = BlobServiceClientBuilder()
            .connectionString(properties.storage.azureConnectionString)
            .buildClient()

        return blobServiceClient.getBlobContainerClient(properties.storage.containerName)
    }
}