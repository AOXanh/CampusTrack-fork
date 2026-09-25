package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.Models.Hello;
import com.jabai.campustrack.Services.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final HelloService helloService = new HelloService();

  @GetMapping("/hello-world")
  public Hello helloWorld() {
    return helloService.sayHelloWorld();
  }
}
