package com.milfist.cache.redis;

public class CacheManagerException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public CacheManagerException(Throwable cause) {
    super(cause);
  }

  public CacheManagerException(String message) {
    super(message);
  }
}

