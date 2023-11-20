package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawCommandValidatorTest {
	private final String ID_3 = "22222222";
	private final String ID = "12345678";
	private final String ID_2 = "11111111";
	private final double APR = 2;
	private final double BALANCE = 1000;
	CommandValidator commandValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		commandValidator = new CommandValidator(bank);
		bank.openCheckingAccount(ID, APR);
		bank.openSavingsAccount(ID_2, APR);
		bank.openCDAccount(ID_3, APR, BALANCE);

	}

	@Test
	void withdrawal_command_is_valid() {
		boolean actual = commandValidator.validate("withdraw 12345678 200");
		assertTrue(actual);
	}

	@Test
	void withdrawal_command_does_not_have_enough_components() {
		boolean actual = commandValidator.validate("withdraw 200");
		assertFalse(actual);
	}

	@Test
	void withdraw_command_has_extra_components() {
		boolean actual = commandValidator.validate("withdraw 12345678 200 123");
		assertFalse(actual);
	}

	@Test
	void withdraw_command_is_case_insensitive() {
		boolean actual = commandValidator.validate("WithdRAW 12345678 200");
		assertTrue(actual);
	}

	@Test
	void withdraw_command_cannot_have_typos() {
		boolean actual = commandValidator.validate("w1thdraw 12345678 200");
		assertFalse(actual);
	}

	@Test
	void withdraw_command_needs_to_exist() {
		boolean actual = commandValidator.validate("12345678 200");
		assertFalse(actual);
	}

	@Test
	void withdraw_command_cant_have_whitespace_before_it() {
		boolean actual = commandValidator.validate("  withdraw 12345678 200");
		assertFalse(actual);
	}

	@Test
	void withdraw_command_cant_have_whitespace_between() {
		boolean actual = commandValidator.validate("withdraw   12345678 200");
		assertFalse(actual);
	}

	@Test
	void withdraw_amount_cant_be_more_than_max_value_for_savings() {
		boolean actual = commandValidator.validate("withdraw 11111111 1001");
		assertFalse(actual);
	}

	@Test
	void withdraw_amount_accepts_max_value_for_savings() {
		boolean actual = commandValidator.validate("withdraw 11111111 1000");
		assertTrue(actual);
	}

	@Test
	void withdraw_amount_accepts_below_max_value_for_savings() {
		boolean actual = commandValidator.validate("withdraw 11111111 999");
		assertTrue(actual);
	}

	@Test
	void withdraw_amount_cant_be_negative_for_savings() {
		boolean actual = commandValidator.validate("withdraw 11111111 -1");
		assertFalse(actual);
	}

	@Test
	void withdraw_amount_accepts_min_value_for_savings() {
		boolean actual = commandValidator.validate("withdraw 11111111 0");
		assertTrue(actual);
	}

	@Test
	void withdraw_amount_accepts_above_min_value_for_savings() {
		boolean actual = commandValidator.validate("withdraw 11111111 1");
		assertTrue(actual);
	}

	@Test
	void withdraw_amount_cant_be_negative_for_checking() {
		boolean actual = commandValidator.validate("withdraw 12345678 -1");
		assertFalse(actual);
	}

	@Test
	void withdraw_amount_cant_be_more_than_max_value_for_checking() {
		boolean actual = commandValidator.validate("withdraw 12345678 401");
		assertFalse(actual);
	}

	@Test
	void withdraw_amount_accepts_max_value_for_checking() {
		boolean actual = commandValidator.validate("withdraw 12345678 400");
		assertTrue(actual);
	}

	@Test
	void withdraw_amount_accepts_below_max_value_for_checking() {
		boolean actual = commandValidator.validate("withdraw 12345678 399");
		assertTrue(actual);
	}

	@Test
	void withdraw_amount_must_be_digits() {
		boolean actual = commandValidator.validate("withdraw 12345678 2dv0");
		assertFalse(actual);
	}

	@Test
	void withdraw_amount_can_be_zero_for_checking() {
		boolean actual = commandValidator.validate("withdraw 12345678 0");
		assertTrue(actual);
	}

	@Test
	void cant_withdraw_without_adding_account_in_bank() {
		boolean actual = commandValidator.validate("deposit 22222222 100");
		assertFalse(actual);
	}

	@Test
	void max_one_withdrawal_per_month_from_savings_account() {
		bank.withdrawMoneyThroughBank(ID_2, 200);
		boolean actual = commandValidator.validate("withdraw 11111111 200");
		assertFalse(actual);

	}

	@Test
	void savings_account_can_withdraw_if_one_month_passed_after_previous_withdrawal() {
		bank.addMoneyThroughBank(ID_2, 600);
		bank.withdrawMoneyThroughBank(ID_2, 200);
		bank.passTime(1);
		boolean actual = commandValidator.validate("withdraw 11111111 200");
		assertTrue(actual);
	}

	@Test
	void CD_withdrawal_invalid_if_age_below_12_months() {
		bank.passTime(2);
		boolean actual = commandValidator.validate("withdraw 22222222 1000");
		assertFalse(actual);
	}

	@Test
	void CD_withdrawal_valid_if_age_above_12_months() {
		bank.passTime(12);
		boolean actual = commandValidator.validate("withdraw 22222222 2000");
		assertTrue(actual);
	}

	@Test
	void CD_can_withdraw_equal_to_balance() {
		bank.passTime(12);
		double balance = bank.getAccountById(ID_3).getBalance();
		String strBalance = Double.toString(balance);
		boolean actual = commandValidator.validate("withdraw 22222222 " + strBalance);
		assertTrue(actual);
		System.out.println(strBalance);
	}

	@Test
	void CD_can_withdraw_more_than_balance() {
		bank.passTime(12);
		double balance = bank.getAccountById(ID_3).getBalance() + 10;
		String strBalance = Double.toString(balance);
		boolean actual = commandValidator.validate("withdraw 22222222 " + strBalance);
		assertTrue(actual);
	}

	@Test
	void CD_cannot_withdraw_less_than_balance() {
		bank.passTime(12);
		boolean actual = commandValidator.validate("withdraw 22222222 100");
		assertFalse(actual);
	}

}
