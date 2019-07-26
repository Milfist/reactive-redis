package com.milfist.cache.redis.mock;

import com.gft.data.DataSystemWrapper;
import com.milfist.cache.redis.CacheManagerException;
import com.milfist.cache.redis.domain.Figure;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Component("figureMock")
public class FigureMock implements DataSystemWrapper<String, Figure, Object> {
//  @Override
//  public Mono<Figure> get(String s, Object... objects) {
//    try {
//      throw new NullPointerException("TEST");
//    } catch (NullPointerException e) {
//      throw new CacheManagerException(e.getMessage());
//    }
////    return null;
//  }
  @Override
  public Mono<Figure> get(Optional<String> s, Object... objects) {
    return Mono.just(new Figure(UUID.randomUUID().toString()));
  }
}
