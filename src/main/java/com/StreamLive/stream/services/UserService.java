package com.StreamLive.stream.services;

import com.StreamLive.stream.models.User;
import com.StreamLive.stream.models.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserBySessionId(String sessionId){
        // Logique pour obtenir un utilisateur en fonction de l'ID de session
        return userRepository.findBySessionId(sessionId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }
    public User createUser(User user){
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
