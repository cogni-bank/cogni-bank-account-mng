package com.cognibank.accountmanagment.cognibankaccountmanagment;

import com.cognibank.accountmanagment.cognibankaccountmanagment.Module.Account;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CogniBankAccountManagmentApplicationTests {



	// To verify that user can create account for the given Id;

	@Test

	public void accountCreateTest() {
		Account account = new Account();
		account.createAccount();
		Assert.assertNotNull(account);

	}

}
