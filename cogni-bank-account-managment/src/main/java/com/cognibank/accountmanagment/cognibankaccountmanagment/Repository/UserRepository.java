package com.cognibank.accountmanagment.cognibankaccountmanagment.Repository;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {


}
