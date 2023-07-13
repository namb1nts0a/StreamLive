package com.StreamLive.stream.controllers;

import com.StreamLive.stream.models.User;
import com.StreamLive.stream.models.UserRepository;
import com.StreamLive.stream.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    private String generateSessionId(){
        return UUID.randomUUID().toString();
    }

//    @PostMapping
//    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
//        String sessionId = generateSessionId();
//        user.setSessionId(sessionId);
//
//        User createdUser = userService.createUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//    }

    @GetMapping("/inscription")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "inscription";
    }

    @PostMapping("/inscription")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "inscription";
        }

        // Effectue les opérations nécessaires pour créer un utilisateur
        String sessionId = generateSessionId();
        user.setSessionId(sessionId);

        User createdUser = userService.createUser(user);

        // Redirige vers une page de confirmation ou une autre page appropriée
        return "redirect:/login";
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws UserPrincipalNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            return ResponseEntity.ok(user);
        }else{
            throw new UserPrincipalNotFoundException("Utilisateur non trouve avec l'id : "+ id);
        }
    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
//        Optional<User> optionalUser = userRepository.findById(id);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            user.setUsername(updatedUser.getUsername());
//            // Met à jour les autres champs si nécessaire
//            User savedUser = userRepository.save(user);
//            return ResponseEntity.ok(savedUser);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        Optional<User> optionalUser = userRepository.findById(id);
//        if (optionalUser.isPresent()) {
//            userRepository.deleteById(id);
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @ExceptionHandler(UserPrincipalNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserPrincipalNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
