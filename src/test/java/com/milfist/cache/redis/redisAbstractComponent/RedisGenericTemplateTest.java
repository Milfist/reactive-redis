package com.milfist.cache.redis.redisAbstractComponent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class RedisGenericTemplateTest {

  @Mock
  private ReactiveRedisConnectionFactory connectionFactoryMock;

  @Mock
  private ReactiveRedisTemplate<String, String> reactiveRedisTemplateMock;

  private RedisGenericTemplate<String, String> redisGenericTemplate;

  @Before
  public void setup() {
    redisGenericTemplate = spy(new RedisGenericTemplate<>(String.class, connectionFactoryMock));
    when(redisGenericTemplate.getReactiveJsonGenericRedisTemplate()).thenReturn(reactiveRedisTemplateMock);
  }

  @Test
  public void check_is_created_OK_and_getReactiveJsonGenericRedisTemplate_is_called_once() {
    ReactiveRedisTemplate<String , String> result = redisGenericTemplate.getReactiveJsonGenericRedisTemplate();
    Assert.assertNotNull(result);
    verify(redisGenericTemplate, times(1)).getReactiveJsonGenericRedisTemplate();
  }
}
