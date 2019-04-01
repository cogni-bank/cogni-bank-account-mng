package com.cognibank.accountmanagment.cognibankaccountmanagment.Repository;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Account, String> {


}
