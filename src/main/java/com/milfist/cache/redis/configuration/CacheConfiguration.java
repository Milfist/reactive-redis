package com.milfist.cache.redis.configuration;

import com.milfist.cache.redis.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveKeyCommands;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveStringCommands;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PreDestroy;

@Configuration
public class CacheConfiguration {

  @Autowired
  private RedisConnectionFactory factory;

//  private ReactiveRedisConnectionFactory factory;
//
//  @Value("${spring.redis.host:localhost}")
//  private String host;
//
//  @Value("${spring.redis.port:6379}")
//  private Integer port;
//
//  @Bean("factory")
//  public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
//    factory = new LettuceConnectionFactory(host, port);
//    return factory;
//  }

//  @Bean
//  public LettuceConnectionFactory redisConnectionFactory() {
//    return new LettuceConnectionFactory();
//  }

  @Bean
  public ReactiveRedisTemplate<String, String> reactiveRedisTemplateString (ReactiveRedisConnectionFactory factory) {
    return new ReactiveRedisTemplate<>(factory, RedisSerializationContext.string());
  }

  @Bean
  public ReactiveRedisTemplate<String, Person> reactiveJsonPersonRedisTemplate(
      ReactiveRedisConnectionFactory factory) {

    Jackson2JsonRedisSerializer<Person> serializer = new Jackson2JsonRedisSerializer<>(Person.class);
    RedisSerializationContext.RedisSerializationContextBuilder<String, Person> builder = RedisSerializationContext
        .newSerializationContext(new StringRedisSerializer());

    RedisSerializationContext<String, Person> serializationContext = builder.value(serializer).build();

    return new ReactiveRedisTemplate<>(factory, serializationContext);
  }

  @Bean
  public ReactiveKeyCommands keyCommands(final ReactiveRedisConnectionFactory factory) {
    return factory.getReactiveConnection()
        .keyCommands();
  }

  @Bean
  public ReactiveStringCommands stringCommands(final ReactiveRedisConnectionFactory factory) {
    return factory.getReactiveConnection()
        .stringCommands();
  }

  public @PreDestroy
  void flushTestDb() {
    factory.getConnection().flushDb();
  }
}
