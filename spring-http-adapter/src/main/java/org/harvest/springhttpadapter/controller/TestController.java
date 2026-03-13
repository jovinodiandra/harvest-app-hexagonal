package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Hidden
public class TestController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
