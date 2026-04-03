package com.itd.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Configuration
class CacheConfig {

    @Bean
    fun redisCacheManagerBuilderCustomizer(
        objectMapper: ObjectMapper,
    ): RedisCacheManagerBuilderCustomizer {
        val cacheObjectMapper = objectMapper.copy().apply {
            activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.PROPERTY
            )
        }

        val serializer = GenericJackson2JsonRedisSerializer(cacheObjectMapper)
        val pair = RedisSerializationContext.SerializationPair.fromSerializer(serializer)

        return RedisCacheManagerBuilderCustomizer { builder ->
            builder
                .withCacheConfiguration(
                    "weather",
                    RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(pair)
                        .entryTtl(Duration.ofMinutes(10))
                )
                .withCacheConfiguration(
                    "trafficFlow",
                    RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(pair)
                        .entryTtl(Duration.ofMinutes(2))
                )
                .withCacheConfiguration(
                    "trafficIncidents",
                    RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(pair)
                        .entryTtl(Duration.ofMinutes(2))
                )
        }
    }
}