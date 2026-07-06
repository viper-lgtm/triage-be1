package cz.martinvedra.triagebe1.config.redis

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig {

    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        val cfg = RedisStandaloneConfiguration()
        cfg.hostName = "localhost"
        cfg.port = 6379
        return LettuceConnectionFactory(cfg)
    }

    @Bean
    fun redisTemplate(connectionFactory: LettuceConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = connectionFactory

        val keySerializer = StringRedisSerializer()
        val valueSerializer = StringRedisSerializer()

        template.keySerializer = keySerializer
        template.hashKeySerializer = keySerializer
        template.valueSerializer = valueSerializer
        template.hashValueSerializer = valueSerializer

        template.afterPropertiesSet()
        return template
    }

    @Bean
    fun cacheManager(connectionFactory: LettuceConnectionFactory): RedisCacheManager {
        val pair: RedisSerializationContext.SerializationPair<String> = RedisSerializationContext.SerializationPair.fromSerializer(
            StringRedisSerializer()
        )

        val config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))
            .serializeValuesWith(pair)

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build()
    }
}