package com.milfist.cache.redis.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Slf4j
@Configuration
@Order(1)
public class CacheConfiguration {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private Integer port;

  @Value("${spring.redis.password}")
  private String password;

  @Primary
  @Bean("connectionFactory")
  public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {

    log.info("Cloud Config Redis");
    log.info("REDIS HOST:" + host);
    log.info("REDIS PORT:" + port);
    log.info("REDIS PASS:" + password);

    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
    configuration.setPassword(password);

    return new LettuceConnectionFactory(configuration);
  }
}
