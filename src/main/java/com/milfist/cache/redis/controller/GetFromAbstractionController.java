package com.milfist.cache.redis.controller;

import com.gft.data.DataSystemWrapper;
import com.milfist.cache.redis.domain.Figure;
import com.milfist.cache.redis.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/redis")
public class GetFromAbstractionController {

  @Autowired
  @Qualifier("personRedisImpl")
  private DataSystemWrapper<String, Person, Object> personSystem;

  @Autowired
  @Qualifier("figureRedisImpl")
  private DataSystemWrapper<String, Figure, Object> figureSystem;

  @RequestMapping(value = "/person/abstract/{id}", method = RequestMethod.GET)
  public Mono<Person> getPerson(@PathVariable String id) {
    return personSystem.get(Optional.of(id));
  }

  @RequestMapping(value = "/figure/abstract/{id}", method = RequestMethod.GET)
  public Mono<Figure> getFigure(@PathVariable String id) {
    return figureSystem.get(Optional.of(id));
  }
}
