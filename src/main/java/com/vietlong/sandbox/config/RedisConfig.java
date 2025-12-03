package com.vietlong.sandbox.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  // Cấu hình RedisTemplate để thao tác thủ công với Redis
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    // Khởi tạo RedisTemplate
    RedisTemplate<String, Object> template = new RedisTemplate<>();

    // Tạo serializer cho key và value
    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

    // Thiết lập kết nối Redis
    template.setConnectionFactory(factory);

    // Key lưu dưới dạng chuỗi
    template.setKeySerializer(stringSerializer);
    // Value lưu dưới dạng JSON
    template.setValueSerializer(jsonSerializer);

    // Key trong hash lưu dưới dạng chuỗi
    template.setHashKeySerializer(stringSerializer);
    // Value trong hash lưu dưới dạng JSON
    template.setHashValueSerializer(jsonSerializer);

    // Đảm bảo template được khởi tạo đầy đủ
    template.afterPropertiesSet();

    // Trả về bean RedisTemplate
    return template;
  }

  @Bean
  // Cấu hình RedisCacheManager dùng cho @Cacheable, @CachePut, @CacheEvict
  public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
    // Tạo cấu hình cache mặc định
    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
        // TTL mặc định cho cache là 30 phút
        .entryTtl(Duration.ofMinutes(30))
        // Key cache lưu dạng chuỗi
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        // Value cache lưu dạng JSON
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

    // Tạo cache manager với cấu hình trên
    return RedisCacheManager.builder(factory)
        .cacheDefaults(config)
        .build();
  }
}
