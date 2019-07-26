package com.milfist.cache.redis.mock;

import com.gft.data.DataSystemWrapper;
import com.milfist.cache.redis.domain.Person;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component("personMock")
public class PersonMock implements DataSystemWrapper<String, Person, Object> {
  @Override
  public Mono<Person> get(Optional<String> s, Object... objects) {
    return Mono.just(new Person("Mock", "eado"));
  }
}
