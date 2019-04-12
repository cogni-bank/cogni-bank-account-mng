package com.cognibank.accountmanagment.cognibankaccountmanagment.Repository;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Account;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.AccountType;
import com.cognibank.accountmanagment.cognibankaccountmanagment.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query(value = "select * from transaction where customer_id = :customer_id",nativeQuery = true)
    List<Transaction> findTransactionsByCustomerId(@Param("customer_id") String id);


    @Query(value = "SELECT *, a.account_number FROM transaction t " +
            "LEFT JOIN account a ON :account_number = a.account_number;",nativeQuery = true)
    List<Transaction> findTransactionsByAccountNumber(@Param("account_number") long accountNumber);

    Optional<List<Transaction>> findByAccountId(String accountId);

    @Query(value = "SELECT * FROM transaction where account_id = :accountId AND destination_account= :destinationAccountId",nativeQuery = true)
    List<Transaction> findTransactionsIdByAccountIdAndDestinationAccountId(@Param("accountId") String accountId, @Param("destinationAccountId") String destinationAccountId);
}
