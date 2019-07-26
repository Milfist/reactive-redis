package com.milfist.cache.redis.redisAbstractComponent;

import com.gft.data.DataSystemWrapper;
import com.milfist.cache.redis.CacheManagerException;
import io.lettuce.core.RedisConnectionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisImplTest {

  private static final String ID = "0001";
  private static final String RESULT = "Hello world!";

  @Mock
  @Qualifier("entitiesRepositoryWrapper")
  private DataSystemWrapper<String, String, String> repositoryWrapper;

  @Mock
  private ReactiveRedisOperations<String, String> operations;

  @Mock
  private ReactiveValueOperations<String, String> valueOperations;


  private DataSystemWrapper<String, String, String> cacheGenericService;


  @Before
  public void setup() {
    when(operations.opsForValue()).thenReturn(valueOperations);
    doReturn(getMonoStringData()).when(valueOperations).get(anyString());
    cacheGenericService = new RedisImpl<>(operations, repositoryWrapper, String.class);
    ReflectionTestUtils.setField(cacheGenericService, "timeToLive", 600);
  }

  @Test
  public void result_is_OK_when_get_data_from_cache_with_a_correct_key() {

    Mono<String> result = cacheGenericService.get(Optional.of(ID));
    String stringFromMono = result.block();

    Assert.assertEquals(RESULT, stringFromMono);
  }

  @Test
  public void result_is_OK_when_get_data_from_cache_with_null_key() {

    Mono<String> result = cacheGenericService.get(Optional.empty());
    String stringFromMono = result.block();

    Assert.assertEquals(RESULT, stringFromMono);
  }

  @Test
  public void result_is_NULL_when_key_is_not_in_cache_and_there_is_no_real_data_system() {
    doReturn(Mono.empty()).when(valueOperations).get(anyString());
    cacheGenericService = new RedisImpl<>(operations, null, String.class);

    Mono<String> result = cacheGenericService.get(Optional.of(ID));
    String stringFromMono = result.block();

    Assert.assertNull(stringFromMono);
  }

  @Test
  public void get_data_from_real_data_system_and_cache_it_with_correct_key() {
    doReturn(Mono.empty()).when(valueOperations).get(anyString());
    doReturn(getMonoStringData()).when(repositoryWrapper).get(any());
    doReturn(Mono.just(Boolean.TRUE)).when(valueOperations).setIfAbsent(anyString(), anyString(), any(Duration.class));

    Mono<String> result = cacheGenericService.get(Optional.of(ID));
    String stringFromMono = result.block();

    Assert.assertEquals(RESULT, stringFromMono);
  }

  @Test
  public void get_data_from_real_data_system_and_cache_it_with_null_key() {
    doReturn(Mono.empty()).when(valueOperations).get(anyString());
    doReturn(getMonoStringData()).when(repositoryWrapper).get(any());
    doReturn(Mono.just(Boolean.TRUE)).when(valueOperations).setIfAbsent(anyString(), anyString(), any(Duration.class));

    Mono<String> result = cacheGenericService.get(Optional.empty());
    String stringFromMono = result.block();

    Assert.assertEquals(RESULT, stringFromMono);
  }

  @Test
  public void get_data_from_real_data_system_and_no_cache_it() {
    doReturn(Mono.empty()).when(valueOperations).get(anyString());
    doReturn(getMonoStringData()).when(repositoryWrapper).get(any());
    doReturn(Mono.just(Boolean.FALSE)).when(valueOperations).setIfAbsent(anyString(), anyString(), any(Duration.class));

    Mono<String> result = cacheGenericService.get(Optional.empty());
    String stringFromMono = result.block();

    Assert.assertEquals(RESULT, stringFromMono);
  }

  @Test
  public void when_get_from_cache_return_mono_error_to_manager_and_real_system_data() {
    doReturn(Mono.error(new RedisConnectionException("Unable to connect to Redis"))).when(valueOperations).get(anyString());
    doReturn(getMonoStringData()).when(repositoryWrapper).get(any());

    Mono<String> result = cacheGenericService.get(Optional.empty());
    String stringFromMono = result.block();

    Assert.assertEquals(RESULT, stringFromMono);
  }

  @Test(expected = CacheManagerException.class)
  public void when_cache_return_empty_and_real_data_system_return_exception_cache_manager_return_exception() {
    doReturn(Mono.empty()).when(valueOperations).get(anyString());
    doReturn(Mono.error(new Exception("Error"))).when(repositoryWrapper).get(any());

    cacheGenericService.get(Optional.of(ID)).block();
  }

  @Test(expected = CacheManagerException.class)
  public void cache_return_exception_data_system_return_exception_cache_manager_return_exception() {
    doReturn(Mono.error(new RedisConnectionException("Unable to connect to Redis"))).when(valueOperations).get(anyString());
    doReturn(Mono.error(new Exception("Error"))).when(repositoryWrapper).get(any());

    cacheGenericService.get(Optional.of(ID)).block();
  }

  @Test
  public void when_set_data_in_cache_return_error_manager_return_data_from_real_system_data() {
    doReturn(Mono.empty()).when(valueOperations).get(anyString());
    doReturn(getMonoStringData()).when(repositoryWrapper).get(any());
    doReturn(Mono.error(new RedisConnectionException("Unable to connect to Redis"))).when(valueOperations).setIfAbsent(anyString(), anyString(), any(Duration.class));

    Mono<String> result = cacheGenericService.get(Optional.empty());
    String stringFromMono = result.block();

    Assert.assertEquals(RESULT, stringFromMono);
  }


  private Mono<String> getMonoStringData() {
    return Mono.just(getObject());
  }

  private String getObject() {
    return RESULT;
  }

}
