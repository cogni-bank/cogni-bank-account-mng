package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public void createUser(User newUser) {
        userRepository.save(newUser);
    }

    public void findUserById(String id) {
        userRepository.findById(id).orElse(null);
    }
    public List<User> getAllUsers(){

        return userRepository.findAll();
    }

    public void update(User user){
        userRepository.save(user);
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }
}
