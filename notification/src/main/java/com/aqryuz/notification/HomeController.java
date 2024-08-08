package com.aqryuz.notification;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  @GetMapping("/")
  public String home() {
    return "index.html";
  }

  @GetMapping("/notify")
  public String notification() {
    return "user-notify.html";
  }
}
