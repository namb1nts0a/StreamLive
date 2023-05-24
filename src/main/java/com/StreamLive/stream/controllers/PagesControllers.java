package com.StreamLive.stream.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesControllers {
    @GetMapping("/")
    public String home(){
        return "index";
    }
}
