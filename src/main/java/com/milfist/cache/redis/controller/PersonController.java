package com.milfist.cache.redis.controller;

import com.milfist.cache.redis.domain.Person;
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
public class PersonController {

  private ReactiveRedisOperations<String, Person> personOperations;

  private ReactiveListOperations<String, Person> listOperations;

  public PersonController(ReactiveRedisOperations<String, Person> personOperations) {
    this.personOperations = personOperations;
    this.listOperations = this.personOperations.opsForList();
  }

  @RequestMapping(value = "/person/{id}", method = RequestMethod.POST)
  public Mono<Boolean> postPerson(@PathVariable String id, @RequestBody Person person) {
    return personOperations.opsForValue().set(id, person).log("set " + id);
  }

  @RequestMapping(value = "/person/{id}", method = RequestMethod.GET)
  public Mono<Person> getPerson(@PathVariable String id) {
    return personOperations.opsForValue().get(id).log("get person " + id);
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
