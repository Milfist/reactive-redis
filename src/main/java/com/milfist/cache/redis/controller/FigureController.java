package com.milfist.cache.redis.controller;

import com.milfist.cache.redis.domain.Figure;
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
public class FigureController {

  private ReactiveRedisOperations<String, Figure> figureOperations;

  public FigureController(ReactiveRedisOperations<String, Figure> figureOperations) {
    this.figureOperations = figureOperations;
  }

  @RequestMapping(value = "/figure/{id}", method = RequestMethod.POST)
  public Mono<Boolean> postFigure(@PathVariable String id, @RequestBody Figure figure) {
    return figureOperations.opsForValue().set(id, figure).log("set " + id);
  }

  @RequestMapping(value = "/figure/{id}", method = RequestMethod.GET)
  public Mono<Figure> getFigure(@PathVariable String id) {
    return figureOperations.opsForValue().get(id).log("get figure " + id);
  }
}
