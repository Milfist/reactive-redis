package com.milfist.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.springframework.data.redis.core.ReactiveListOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.ByteBuffer;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Show usage of reactive Template API on Redis lists using {@link ReactiveRedisOperations} with Jackson serialization.
 *
 * @author Mark Paluch
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class JacksonJsonTests {

  @Autowired
  private ReactiveRedisOperations<String, Person> typedOperations;

  private ReactiveListOperations<String, Person> listOperations;

  @Before
  public void setup() {
    listOperations = typedOperations.opsForList();
  }

  @Test
  public void shouldWriteAndReadPerson() {


//    Mono<Long> lPush = listOperations.leftPush("Miguel", new Person("Miguel", "Anguita")).log("¿¿Push??");
    listOperations.set("13324234", 1000, new Person("Miguel", "Anguita")).log("Jarr");

//    StepVerifier.create(lPush)
//        .expectNext(2L)
//        .verifyComplete();

//    typedOperations.opsForValue().set("Miguel", new Person("Miguel", "Anguita"));


//    Mono<Person> m = typedOperations.opsForValue().get("Miguel");

//    String name = m.block().firstname;

//    StepVerifier.create(typedOperations.opsForValue().set("homer", new Person("Homer", "Simpson"))) //
//        .expectNext(true) //
//        .verifyComplete();
//
//    Flux<String> get = typedOperations.execute(conn -> conn.stringCommands().get(ByteBuffer.wrap("homer".getBytes()))) //
//        .map(ByteUtils::getBytes) //
//        .map(String::new);
//
//    StepVerifier.create(get) //
//        .expectNext("{\"firstname\":\"Homer\",\"lastname\":\"Simpson\"}") //
//        .verifyComplete();
//
//    StepVerifier.create(typedOperations.opsForValue().get("homer")) //
//        .expectNext(new Person("Homer", "Simpson")) //
//        .verifyComplete();
  }

//  @Test
//  public void shouldWriteAndReadPersonObject() {
//
//    StepVerifier.create(genericOperations.opsForValue().set("homer", new Person("Homer", "Simpson"))) //
//        .expectNext(true) //
//        .verifyComplete();
//
//    Flux<String> get = genericOperations.execute(conn -> conn.stringCommands().get(ByteBuffer.wrap("homer".getBytes()))) //
//        .map(ByteUtils::getBytes) //
//        .map(String::new);
//
//    StepVerifier.create(get) //
//        .expectNext("{\"_type\":\"example.springdata.redis.Person\",\"firstname\":\"Homer\",\"lastname\":\"Simpson\"}") //
//        .verifyComplete();
//
//    StepVerifier.create(genericOperations.opsForValue().get("homer")) //
//        .expectNext(new Person("Homer", "Simpson")) //
//        .verifyComplete();
//  }

}
