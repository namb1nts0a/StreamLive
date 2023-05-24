package com.StreamLive.stream.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PagesControllers {
    @GetMapping("/")
    public String home(){
        return "index";
    }
}
