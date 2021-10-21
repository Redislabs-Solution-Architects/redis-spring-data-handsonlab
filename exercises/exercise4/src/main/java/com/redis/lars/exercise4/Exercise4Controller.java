package com.redis.lars.exercise4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Exercise4Controller {
    

    @GetMapping
    public String hello()   {
        return "Hello Spring Sesion Redis world!";
    }

}
