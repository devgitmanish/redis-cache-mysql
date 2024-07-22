package com.redis.mysql.service;

import com.redis.mysql.entity.User;
import com.redis.mysql.repository.UserRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRespository userRespository;

    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id){
        delayThread();
        return userRespository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id));
    }

    @Cacheable(value ="allUsers")
    public List<User> getAllUsers(){
        System.out.println("fetch all users data");
        return userRespository.findAll();
    }

    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user, Long id){
        return userRespository.findById(id)
                .map(existingUser ->{
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    return userRespository.save(existingUser);
                })
                .orElseThrow(()-> new RuntimeException("user not found with id: {}"+ id));
    }

    public User saveUser(User user){
        return userRespository.save(user);
    }

    @Caching(evict = {
            @CacheEvict(value = "users", key = "#id"),
            @CacheEvict(value = "allUsers", allEntries = true, key = "#id")
    })
    public void deleteUser(Long id){
        System.out.println("user removed by id : "+ id);
        userRespository.deleteById(id);
    }

    public void delayThread(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
