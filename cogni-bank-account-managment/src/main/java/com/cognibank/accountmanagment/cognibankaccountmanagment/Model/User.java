package com.cognibank.accountmanagment.cognibankaccountmanagment.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

//    @GeneratedValue(generator= "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name ="ID", columnDefinition ="VARCHAR(255)")
//    private String id;
//
    @Id
    @Column(nullable = false)
    private String Id;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Account> accounts;


    public String getUserId() {
        return Id;
    }

    public User withUserId(String userId) {
        this.Id = userId;
        return this;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public User withAccount(List<Account> accounts) {
        this.accounts = accounts;
        return this;
    }

}
