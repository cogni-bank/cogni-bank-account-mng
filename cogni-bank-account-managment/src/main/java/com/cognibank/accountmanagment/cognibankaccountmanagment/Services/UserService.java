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
    private UserRepository userRepository;


    public void createUser(User newUser) {
        userRepository.save(newUser);
    }


    public boolean findUserById(String id) {

        return userRepository.findById(id) != null ? true : false;

    }

    public Optional<User> findUser(String id) {

        return userRepository.findById(id) ;

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
