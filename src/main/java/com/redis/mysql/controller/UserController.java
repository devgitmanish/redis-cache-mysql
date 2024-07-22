package com.redis.mysql.controller;

import com.redis.mysql.entity.User;
import com.redis.mysql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        return user !=null
                ? ResponseEntity.of(Optional.of(user))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> user = userService.getAllUsers();
        return user !=null
                ? ResponseEntity.of(Optional.of(user))
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @PostMapping("/save-user")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        User saveUser = userService.saveUser(user);
        return saveUser!=null
                ? new ResponseEntity<>(saveUser, HttpStatus.CREATED)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @PutMapping("update-user/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user,
                                           @PathVariable Long id){
        User responseUser = userService.updateUser(user, id);
        return responseUser!=null
                ? new ResponseEntity<>(responseUser, HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
