package cz.martinvedra.triagebe1.config.properties

import cz.martinvedra.triagebe1.config.properties.AppConfigurationProperties.Companion.PREFIX_NAME
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(PREFIX_NAME)
class AppConfigurationProperties(
    val enabled: Boolean = true,
    val be2: Be2Properties
) {
    companion object {
        const val PREFIX_NAME = "app"
    }

    class Be2Properties(
        val baseUrl: String
    )
}