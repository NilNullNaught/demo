package com.heyunetwork.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("helloworld")
    public String helloworld(){
        return "helloworld"+ LocalDateTime.now();
    }

}
