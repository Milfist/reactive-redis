package com.milfist.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/redis")
public class RedisController {

  private ReactiveRedisOperations<String, Person> typedOperations;

  private ReactiveListOperations<String, Person> listOperations;

  public RedisController(ReactiveRedisOperations<String, Person> typedOperations) {
    this.typedOperations = typedOperations;
    this.listOperations = this.typedOperations.opsForList();
  }

  @RequestMapping(value = "/person/{id}", method = RequestMethod.POST)
  public Mono<Boolean> postPerson(@PathVariable String id, @RequestBody Person person) {
    return typedOperations.opsForValue().set(id, person).log("set " + id);
  }

  @RequestMapping(value = "/person/{id}", method = RequestMethod.GET)
  public Mono<Person> getPerson(@PathVariable String id) {
    return typedOperations.opsForValue().get(id).log("get person " + id);
  }

  @RequestMapping(value = "/person/{id}", method = RequestMethod.DELETE)
  public Mono<ResponseEntity> deletePerson(@PathVariable String id) {
    return listOperations.delete(id).flatMap(result -> {
      if(result) {
        return Mono.just(new ResponseEntity<>(HttpStatus.OK));
      } else {
        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
      }
    });
  }
}
