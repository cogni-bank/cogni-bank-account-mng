package com.cognibank.accountmanagment.cognibankaccountmanagment.Controller;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.AccountNotFoundException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Exceptions.UserAccountNotFoundException;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;


    @PutMapping("/{id}")
    public User createUser(@RequestBody @PathVariable("id") String id, User user) throws AccountNotFoundException {
        user.withUserId(id);
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") String id) throws UserAccountNotFoundException {

        try {
            userService.deleteUser(id);
        }catch (Exception e){
            throw new UserAccountNotFoundException("User not found", e);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id) throws UserAccountNotFoundException {
        User usr = userService.findUserById(id);
        if(id==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(usr);

    }
}
