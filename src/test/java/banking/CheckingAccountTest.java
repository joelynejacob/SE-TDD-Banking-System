package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CheckingAccountTest {
	@Test
	public void starting_balance_of_checking_account_is_zero() {
		CheckingAccount checkingAccount = new CheckingAccount(AccountTest.ID, AccountTest.APR);
		double actual = checkingAccount.getBalance();
		assertEquals(0, actual);

	}
}
