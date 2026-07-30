package bitnagil.global.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {

    /**
     * Jackson 직렬화 문제 해결을 위한 ObjectMapper 커스터마이징
     * Redis Cache 전용 ObjectMapper (전역 빈 아님)
     */
    private ObjectMapper createRedisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.WRAPPER_OBJECT
        );
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Redis 전용이므로 FEIGN, RestTemplate 등에 영향 없음
        return objectMapper;
    }

    /**
     * Spring Boot 가 기본적으로 RedisCacheManager 를 자동 설정해줘서 RedisCacheConfiguration 없어도 사용 가능
     * Bean 을 새로 선언하면 직접 설정한 RedisCacheConfiguration 이 적용
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        ObjectMapper redisObjectMapper = createRedisObjectMapper();
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper))
                );
    }

    /**
     * 여러 Redis Cache 에 관한 설정을 하고 싶다면 RedisCacheManagerBuilderCustomizer 를 사용할 수 있음
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        ObjectMapper redisObjectMapper = createRedisObjectMapper();

        return (builder) -> builder
                .withCacheConfiguration("recommendedRoutine",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofHours(6)) // 6시간 동안 캐시 유지
                                .disableCachingNullValues() // null 값 캐싱 비활성화
                                .serializeKeysWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                                )
                                .serializeValuesWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper))
                                ))
                .withCacheConfiguration("categoryRecommendedRoutine",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofHours(6)) // 6시간 동안 캐시 유지
                                .disableCachingNullValues() // null 값 캐싱 비활성화
                                .serializeKeysWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                                )
                                .serializeValuesWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper))
                                ))
                .withCacheConfiguration("youthPolicyAll",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofHours(26)) // 매일 새벽 갱신 주기보다 길게 유지(갱신 실패 시 stale 서빙)
                                .disableCachingNullValues()
                                .serializeKeysWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                                )
                                .serializeValuesWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper))
                                ));
    }
}
