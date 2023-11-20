package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepositCommandValidatorTest {
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

	}

	@Test
	void deposit_command_is_valid() {
		boolean actual = commandValidator.validate("deposit 12345678 1000.00");
		assertTrue(actual);
	}

	@Test
	void deposit_command_does_not_have_enough_components() {
		boolean actual = commandValidator.validate("deposit 1000");
		assertFalse(actual);
	}

	@Test
	void deposit_command_has_extra_components() {
		boolean actual = commandValidator.validate("deposit 12345678 1000 123");
		assertFalse(actual);
	}

	@Test
	void deposit_command_is_case_insensitive() {
		boolean actual = commandValidator.validate("dePOsit 12345678 1000");
		assertTrue(actual);
	}

	@Test
	void deposit_command_cannot_have_typos() {
		boolean actual = commandValidator.validate("dep0sit 12345678 1000");
		assertFalse(actual);
	}

	@Test
	void deposit_command_needs_to_exist() {
		boolean actual = commandValidator.validate("12345678 1000");
		assertFalse(actual);
	}

	@Test
	void deposit_command_cant_have_whitespace_before_it() {
		boolean actual = commandValidator.validate("  deposit 12345678 1000");
		assertFalse(actual);
	}

	@Test
	void deposit_command_cant_have_whitespace_between() {
		boolean actual = commandValidator.validate("deposit   12345678 1000");
		assertFalse(actual);
	}

	@Test
	void deposit_command_can_have_whitespace_at_end() {
		boolean actual = commandValidator.validate("deposit 12345678 1000   ");
		assertTrue(actual);
	}

	@Test
	void deposit_amount_cant_be_negative_for_checking() {
		boolean actual = commandValidator.validate("deposit 12345678 -1");
		assertFalse(actual);
	}

	@Test
	void deposit_amount_cant_be_more_than_max_value_for_savings() {
		boolean actual = commandValidator.validate("deposit 11111111 2500.001");
		assertFalse(actual);
	}

	@Test
	void deposit_amount_accepts_max_value_for_savings() {
		boolean actual = commandValidator.validate("deposit 11111111 2500");
		assertTrue(actual);
	}

	@Test
	void deposit_amount_accepts_below_max_value_for_savings() {
		boolean actual = commandValidator.validate("deposit 11111111 2499");
		assertTrue(actual);
	}

	@Test
	void deposit_amount_cant_be_negative_for_savings() {
		boolean actual = commandValidator.validate("deposit 11111111 -1");
		assertFalse(actual);
	}

	@Test
	void deposit_amount_accepts_min_value_for_savings() {
		boolean actual = commandValidator.validate("deposit 11111111 0");
		assertTrue(actual);
	}

	@Test
	void deposit_amount_accepts_above_min_value_for_savings() {
		boolean actual = commandValidator.validate("deposit 11111111 1");
		assertTrue(actual);
	}

	@Test
	void deposit_amount_cant_be_more_than_max_value_for_checking() {
		boolean actual = commandValidator.validate("deposit 12345678 1001");
		assertFalse(actual);
	}

	@Test
	void deposit_amount_accepts_max_value_for_checking() {
		boolean actual = commandValidator.validate("deposit 12345678 1000");
		assertTrue(actual);
	}

	@Test
	void deposit_amount_accepts_below_max_value_for_checking() {
		boolean actual = commandValidator.validate("deposit 12345678 999");
		assertTrue(actual);
	}

	@Test
	void deposit_amount_must_be_digits() {
		boolean actual = commandValidator.validate("deposit 12345678 2dv0");
		assertFalse(actual);
	}

	@Test
	void deposit_amount_can_be_zero_for_checking() {
		boolean actual = commandValidator.validate("deposit 12345678 0");
		assertTrue(actual);
	}

	@Test
	void cd_cannot_deposit() {
		bank.openCDAccount(ID, APR, BALANCE);
		boolean actual = commandValidator.validate("deposit 87654321 10");
		assertFalse(actual);
	}

	@Test
	void cant_deposit_without_adding_account_in_bank() {
		boolean actual = commandValidator.validate("deposit 22222222 100");
		assertFalse(actual);
	}
}
