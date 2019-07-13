package com.ca.samples.springresthello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@RestController
public class HelloController {
    @GetMapping("/")
    public String index() {
        return "Welcome to Spring Boot REST Hello Application";

    }
}