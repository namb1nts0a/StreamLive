package com.StreamLive.stream.controllers;

import com.StreamLive.stream.models.User;
import com.StreamLive.stream.models.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class PagesControllers {

    private UserRepository userRepository;

    public PagesControllers(UserRepository userRepository){
        this.userRepository = userRepository;
    }
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

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password){
        if (isValidCredentials(username, password)){
            System.out.println("Connexion login");
            return "redirect:/";
        }else{
            System.out.println("Connexion error");
            return "login";
        }
    }

    private boolean isValidCredentials(String username, String password){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            return user.getPassword().equals(password);
        }
        return false;
    }


}
