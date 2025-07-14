package com.framework.backend;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BaseSecurityApplicationTests {

  @Test
  public void test() {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encode = encoder.encode("123456");
    System.out.println(encode);
  }
}
