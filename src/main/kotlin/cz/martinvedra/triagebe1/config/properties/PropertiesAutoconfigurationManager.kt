package cz.martinvedra.triagebe1.config.properties

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AppConfigurationProperties::class)
@ConditionalOnProperty(prefix = AppConfigurationProperties.PREFIX_NAME, value = ["enabled"], matchIfMissing = true)
class PropertiesAutoconfigurationManager