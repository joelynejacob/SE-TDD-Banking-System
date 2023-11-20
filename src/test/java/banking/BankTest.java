package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankTest {

	public static final String ACCOUNT_ID_1 = "12345678";
	public static final String ACCOUNT_ID_2 = "87654321";

	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
	}

	@Test
	public void bank_has_no_accounts_initially() {
		assertTrue(bank.getAccounts().isEmpty());
	}

	@Test
	public void add_account_to_bank() {
		bank.openCheckingAccount(ACCOUNT_ID_1, AccountTest.APR);
		assertEquals(1, bank.getAccounts().size());

	}

	@Test
	public void add_two_accounts_to_bank() {
		bank.openCheckingAccount(ACCOUNT_ID_1, AccountTest.APR);
		bank.openCheckingAccount(ACCOUNT_ID_2, AccountTest.APR);
		assertEquals(2, bank.getAccounts().size());
	}

	@Test
	public void retrieve_an_account() {
		bank.openCheckingAccount(ACCOUNT_ID_1, AccountTest.APR);
		String actual = ACCOUNT_ID_1;
		assertEquals(actual, bank.getAccounts().get(ACCOUNT_ID_1).getID());
	}

	@Test
	public void bank_can_deposit_money_by_id() {
		bank.openSavingsAccount(ACCOUNT_ID_1, AccountTest.APR);
		bank.addMoneyThroughBank(ACCOUNT_ID_1, AccountTest.TEST_BALANCE);
		double actual = AccountTest.TEST_BALANCE;
		assertEquals(actual, bank.getAccounts().get(ACCOUNT_ID_1).getBalance());
	}

	@Test
	public void bank_can_deposit_money_twice() {
		bank.openSavingsAccount(ACCOUNT_ID_1, AccountTest.APR);
		bank.addMoneyThroughBank(ACCOUNT_ID_1, AccountTest.TEST_BALANCE);
		bank.addMoneyThroughBank(ACCOUNT_ID_1, AccountTest.TEST_BALANCE);
		double actual = 2 * AccountTest.TEST_BALANCE;
		assertEquals(actual, bank.getAccounts().get(ACCOUNT_ID_1).getBalance());
	}

	@Test
	public void bank_can_withdraw_money_by_id() {
		bank.openCDAccount(ACCOUNT_ID_1, AccountTest.APR, AccountTest.TEST_BALANCE);
		bank.withdrawMoneyThroughBank(ACCOUNT_ID_1, AccountTest.TEST_WITHDRAWAL);
		double actual = AccountTest.TEST_BALANCE - AccountTest.TEST_WITHDRAWAL;
		assertEquals(actual, bank.getAccounts().get(ACCOUNT_ID_1).getBalance());
	}

	@Test
	public void bank_can_withdraw_money_twice() {
		bank.openCDAccount(ACCOUNT_ID_1, AccountTest.APR, AccountTest.TEST_BALANCE);
		bank.withdrawMoneyThroughBank(ACCOUNT_ID_1, AccountTest.TEST_WITHDRAWAL);
		bank.withdrawMoneyThroughBank(ACCOUNT_ID_1, AccountTest.TEST_WITHDRAWAL);
		double actual = AccountTest.TEST_BALANCE - 2 * AccountTest.TEST_WITHDRAWAL;
		assertEquals(actual, bank.getAccounts().get(ACCOUNT_ID_1).getBalance());
	}

	@Test
	public void account_exists_in_bank() {
		bank.openCheckingAccount(ACCOUNT_ID_1, AccountTest.APR);
		boolean actual = bank.accountExistsByQuickID(ACCOUNT_ID_1);
		assertTrue(actual);
	}

}
