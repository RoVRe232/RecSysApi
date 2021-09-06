package com.upverasmusproject.ro.recsysrestapi.controllers;


import com.upverasmusproject.ro.recsysrestapi.utils.exceptions.VideoNotFoundException;
import com.upverasmusproject.ro.recsysrestapi.entities.User;
import com.upverasmusproject.ro.recsysrestapi.entities.UserRepository;
import com.upverasmusproject.ro.recsysrestapi.entities.VideoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController{

    private final UserRepository userRepo;

    UserController(VideoRepository repo, UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/users")
    List<User> allUsers(){
        return userRepo.findAll();
    }

    @PostMapping("/users")
    User newUser(@RequestBody User newVideo){
        return userRepo.save(newVideo);
    }

    @GetMapping("/users/{id}")
    User oneUser(@PathVariable Long id){
        return  userRepo.findById(id).orElseThrow(()->new VideoNotFoundException(id));
    }

    @PostMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id){

        return  userRepo.findById(id)
                .map(user -> {
                    user.setLearningType(newUser.getLearningType());
                    user.setQueries(newUser.getQueries());
                    user.setToken(newUser.getToken());
                    user.setUsername(newUser.getUsername());
                    return userRepo.save(user);
                }).orElseGet(()->{
                    newUser.setId(id);
                    return userRepo.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id){
        userRepo.deleteById(id);
    }
}
