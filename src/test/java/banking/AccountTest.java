package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
	public static final String ID = "12345678";
	public static final double APR = 1.2;
	public static final double TEST_BALANCE = 500;
	public static final double TEST_WITHDRAWAL = 100;
	CheckingAccount checkingAccount;

	@BeforeEach
	public void setUp() {
		checkingAccount = new CheckingAccount(ID, APR);
	}

	@Test
	public void money_can_be_deposited_to_account() {
		checkingAccount.depositMoney(TEST_BALANCE);
		double actual = checkingAccount.getBalance();
		assertEquals(TEST_BALANCE, actual);
	}

	@Test
	public void can_deposit_twice_in_account() {
		checkingAccount.depositMoney(TEST_BALANCE);
		checkingAccount.depositMoney(TEST_BALANCE);
		double actual = checkingAccount.getBalance();
		assertEquals(TEST_BALANCE * 2, actual);
	}

	@Test
	public void money_can_be_withdrawn_from_account() {
		checkingAccount.depositMoney(TEST_BALANCE);
		checkingAccount.withdrawMoney(TEST_WITHDRAWAL);
		double actual = checkingAccount.getBalance();
		assertEquals(TEST_BALANCE - TEST_WITHDRAWAL, actual);
	}

	@Test
	public void withdrawing_more_than_balance_returns_zero() {
		checkingAccount.depositMoney(TEST_BALANCE);
		checkingAccount.withdrawMoney(TEST_WITHDRAWAL + 400);
		double actual = checkingAccount.getBalance();
		assertEquals(0, actual);
	}

	@Test
	public void withdrawing_equal_to_balance_returns_zero() {
		checkingAccount.depositMoney(TEST_BALANCE);
		checkingAccount.withdrawMoney(TEST_WITHDRAWAL + 500);
		double actual = checkingAccount.getBalance();
		assertEquals(0, actual);
	}

	@Test
	public void can_withdraw_twice_from_account() {
		checkingAccount.depositMoney(TEST_BALANCE * 2);
		checkingAccount.withdrawMoney(TEST_WITHDRAWAL);
		checkingAccount.withdrawMoney(TEST_WITHDRAWAL);
		double actual = checkingAccount.getBalance();
		assertEquals(TEST_BALANCE * 2 - TEST_WITHDRAWAL * 2, actual);
	}

	@Test
	public void account_apr_is_supplied_value() {
		double actual = checkingAccount.getApr();
		assertEquals(APR, actual);

	}

	@Test
	public void account_returns_age() {
		checkingAccount.increaseAge(1);
		double actual = checkingAccount.getAge();
		assertEquals(actual, 1);
	}

}