package com.milfist.cache.redis.configuration;

import com.gft.data.DataSystemWrapper;
import com.milfist.cache.redis.domain.Figure;
import com.milfist.cache.redis.redisAbstractComponent.RedisImpl;
import com.milfist.cache.redis.domain.Person;
import com.milfist.cache.redis.redisAbstractComponent.RedisGenericTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Configuration
@Primary
@Component
public class RedisConfig {

  @Autowired
  @Qualifier("connectionFactory")
  private ReactiveRedisConnectionFactory connectionFactory;

  @Autowired
  private ReactiveRedisOperations<String, Person> personOperations;

  @Autowired
  private ReactiveRedisOperations<String, Figure> figureOperations;

  @Autowired
  @Qualifier("personMock")
  private DataSystemWrapper<String, Person, Object> mockPerson;

  @Autowired
  @Qualifier("figureMock")
  private DataSystemWrapper<String, Figure, Object> mockFigure;

  public @PreDestroy
  void flushTestDb() {
    connectionFactory.getReactiveConnection().close();
  }

  @Bean(name = "personTemplate")
  public ReactiveRedisTemplate<String, Person> getPersonTemplate() {
    return new RedisGenericTemplate<String, Person>(Person.class, connectionFactory).getReactiveJsonGenericRedisTemplate();
  }

  @Bean(name = "personRedisImpl")
  public DataSystemWrapper<String, Person, Object> getPersonFromCacheService() {
    return new RedisImpl<>(personOperations, mockPerson, Person.class);
  }

  @Bean(name = "figureTemplate")
  public ReactiveRedisTemplate<String, Figure> getFigureTemplate() {
    return new RedisGenericTemplate<String, Figure>(Figure.class, connectionFactory).getReactiveJsonGenericRedisTemplate();
  }

  @Bean(name = "figureRedisImpl")
  public DataSystemWrapper<String, Figure, Object> getFigureFromCacheService() {
    return new RedisImpl<>(figureOperations, mockFigure, Figure.class);
  }
}
