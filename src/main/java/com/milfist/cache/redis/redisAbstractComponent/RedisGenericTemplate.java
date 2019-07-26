package com.milfist.cache.redis.redisAbstractComponent;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisGenericTemplate<K, V> {

  private Class<V> valueType;
  private ReactiveRedisConnectionFactory connectionFactory;

  public RedisGenericTemplate(Class<V> valueType, ReactiveRedisConnectionFactory connectionFactory) {
    this.valueType = valueType;
    this.connectionFactory = connectionFactory;
  }

  public ReactiveRedisTemplate<K, V> getReactiveJsonGenericRedisTemplate() {
    Jackson2JsonRedisSerializer<V> serializer = new Jackson2JsonRedisSerializer<>(valueType);
    RedisSerializationContext.RedisSerializationContextBuilder<K, V> builder = RedisSerializationContext
        .newSerializationContext(new StringRedisSerializer());
    RedisSerializationContext<K, V> serializationContext = builder.value(serializer).build();
    return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
  }


}
