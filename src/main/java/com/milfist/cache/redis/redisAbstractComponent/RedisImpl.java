package com.milfist.cache.redis.redisAbstractComponent;

import com.gft.data.DataSystemWrapper;
import com.milfist.cache.redis.CacheManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Optional;
import java.util.StringJoiner;

@Slf4j
public class RedisImpl<K, V, P> implements DataSystemWrapper<K, V, P> {

  private static final String SET_CACHE_OK = "Data cached in Redis: {0}";
  private static final String NO_REAL_DATA_SYSTEM = "There is no real data system to obtain data.";
  private static final String OBJECT_NOT_FOUND = "Object not found in the cache for id: {0}";
  private static final String GET_CACHE_ERROR = "An error occurred obtaining data from the cache: {0}";
  private static final String SET_CACHE_ERROR = "An error occurred caching data: {0} key: {1} \n TRACE: {2}";
  private static final String GET_REAL_DATA_SYSTEM_ERROR = "An error occurred obtaining data from real data system: {0}";

  @Value("${cache.time-to-live}")
  private Integer timeToLive;

  private DataSystemWrapper<K, V, P> realDataSystem;
  private ReactiveValueOperations<String, V> operations;

  private Class<V> valueType;

  public RedisImpl(ReactiveRedisOperations<String, V> genericOperations,
                   DataSystemWrapper<K, V, P> realDataSystem,
                   Class<V> valueType) {
    this.realDataSystem = realDataSystem;
    this.valueType = valueType;
    this.operations = genericOperations.opsForValue();
  }

  @Override
  public Mono<V> get(Optional<K> k, P... objects) {

    K key = k.orElse(null);

    Mono<V> result = this.getDataFromRedis(key);

    return result.switchIfEmpty(Mono.defer(() -> {
      log.info(MessageFormat.format(OBJECT_NOT_FOUND, key));
      if (this.realDataSystemIsNotNull()) {
        return saveObjectInCache(key, this.getDataFromRealDataSystem(key, objects));
      } else {
        log.info(NO_REAL_DATA_SYSTEM);
        return Mono.empty();
      }
    })).doOnError(ex -> log.error(MessageFormat.format(GET_CACHE_ERROR, ex.getMessage())))
        .onErrorResume(ex -> realDataSystemIsNotNull() ? this.getDataFromRealDataSystem(key, objects) : Mono.error(new CacheManagerException(ex)));
  }

  private Mono<V> getDataFromRedis(K key) {
    return this.operations.get(this.getHashFromKey(key));
  }

  private boolean realDataSystemIsNotNull() {
    return this.realDataSystem != null;
  }

  private Mono<V> getDataFromRealDataSystem(K key, P...objects){
    return this.realDataSystem.get(key == null ? Optional.empty() : Optional.of(key), objects)
        .doOnError(ex -> log.error(MessageFormat.format(GET_REAL_DATA_SYSTEM_ERROR, ex.getMessage())))
        .onErrorResume(ex -> Mono.error(new CacheManagerException(ex)));
  }

  private Mono<V> saveObjectInCache(K key, Mono<V> value) {
    return value.map(v -> {
          this.setDataInRedis(key, v)
          .subscribe(setResult -> log.info(MessageFormat.format(SET_CACHE_OK, setResult)),
              err -> log.error(MessageFormat.format(SET_CACHE_ERROR, v.getClass(), key, err.getMessage())));
      return v;
    });
  }

  private Mono<Boolean> setDataInRedis(K key, V value) {
    return this.operations.setIfAbsent(this.getHashFromKey(key), value, Duration.ofSeconds(this.timeToLive));
  }

  private String getClassName() {
    return this.valueType.getSimpleName();
  }

  private String getHashFromKey(K key) {
    String hash;

    if (checkKeyIsEmpty(key)) {
      hash = getClassName();
    } else {
      hash = getConcatenationFromKeyAndClassName(key);
    }
    return DigestUtils.sha1DigestAsHex(hash);
  }

  private boolean checkKeyIsEmpty(K key) {
    return StringUtils.isEmpty(key);
  }

  private String getConcatenationFromKeyAndClassName(K key) {
    StringJoiner joiner = new StringJoiner("-");
    joiner.add(key.toString());
    joiner.add(getClassName());
    return joiner.toString();
  }
}
