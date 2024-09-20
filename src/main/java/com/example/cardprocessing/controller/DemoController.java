package com.example.cardprocessing.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @GetMapping("/admin")
    public String getAdmin(){
        return "this is admin page";
    }

    @GetMapping("/user")
    public String getUser(){
        return "this is user page";
    }
}


