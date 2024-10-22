package com.noom.interview.fullstack.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/java")
public class JavaController {

    @GetMapping
    public String java(){
        return "Java test is done!!";
    }
}
