package com.milfist.cache.redis.domain;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {

  private String firstName;
  private String lastName;
}

