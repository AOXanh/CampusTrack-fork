package com.jabai.campustrack.Services;

import com.jabai.campustrack.Models.Hello;

public class HelloService {
  public Hello sayHelloWorld() {
    return new Hello("Hello world ;D");
  }
}