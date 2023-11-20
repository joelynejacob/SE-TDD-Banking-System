package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SavingsAccountTest {
	SavingsAccount savingsAccount;

	@BeforeEach
	public void setUp() {
		savingsAccount = new SavingsAccount(AccountTest.ID, AccountTest.APR);
	}

	@Test
	public void starting_balance_of_savings_account_is_zero() {
		double actual = savingsAccount.getBalance();
		assertEquals(0, actual);

	}

}