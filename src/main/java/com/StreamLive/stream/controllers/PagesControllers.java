package com.StreamLive.stream.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesControllers {
    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/users")
    public String users(){
        return  "users";
    }
}
