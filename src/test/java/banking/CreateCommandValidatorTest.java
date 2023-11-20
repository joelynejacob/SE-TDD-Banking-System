package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateCommandValidatorTest {
	private final String ID = "12345678";
	private final double APR = 2.0;
	CommandValidator commandValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		commandValidator = new CommandValidator(bank);
	}

	@Test
	void account_type_command_is_valid() {
		boolean actual = commandValidator.validate("create checking 12345678 0.0");
		assertTrue(actual);
	}

	@Test
	void account_type_command_is_case_insensitive() {
		boolean actual = commandValidator.validate("create CHEckInG 12345678 0.0");
		assertTrue(actual);
	}

	@Test
	void account_type_only_accepts_chars() {
		boolean actual = commandValidator.validate("create check23ing 12345678 0.0");
		assertFalse(actual);
	}

	@Test
	void account_type_does_not_accept_typos() {
		boolean actual = commandValidator.validate("create checkring 12345678 0.0");
		assertFalse(actual);
	}

	@Test
	void account_APR_valid() {
		boolean actual = commandValidator.validate("create checking 12345678 4.0");
		assertTrue(actual);
	}

	@Test
	void account_APR_does_not_have_to_be_floating_point() {
		boolean actual = commandValidator.validate("create checking 12345678 4");
		assertTrue(actual);
	}

	@Test
	void account_APR_can_have_two_decimal_points() {
		boolean actual = commandValidator.validate("create checking 12345678 4.35");
		assertTrue(actual);
	}

	@Test
	void account_APR_can_be_zero() {
		boolean actual = commandValidator.validate("create checking 12345678 0.0");
		assertTrue(actual);
	}

	@Test
	void account_APR_accepts_lower_limit() {
		boolean actual = commandValidator.validate("create checking 12345678 0");
		assertTrue(actual);
	}

	@Test
	void account_APR_accepts_upper_limit() {
		boolean actual = commandValidator.validate("create checking 12345678 10");
		assertTrue(actual);
	}

	@Test
	void account_APR_not_below_0() {
		boolean actual = commandValidator.validate("create checking 12345678 -0.1");
		assertFalse(actual);
	}

	@Test
	void account_APR_not_above_10() {
		boolean actual = commandValidator.validate("create checking 12345678 10.1");
		assertFalse(actual);
	}

	@Test
	void account_APR_does_not_contain_chars() {
		boolean actual = commandValidator.validate("create checking 12345678 1a");
		assertFalse(actual);
	}

	@Test
	void cd_account_starting_balance_does_not_accept_non_digits() {
		boolean actual = commandValidator.validate("create cd 12345678 1 one-thousand");
		assertFalse(actual);
	}

	@Test
	void cd_account_starting_balance_does_not_accept_typos() {
		boolean actual = commandValidator.validate("create cd 12345678 1 10o0");
		assertFalse(actual);
	}

	@Test
	void cd_account_cannot_be_created_without_starting_balance() {
		boolean actual = commandValidator.validate("create cd 12345678 1");
		assertFalse(actual);
	}

	@Test
	void cd_account_starting_balance_cannot_be_0() {
		boolean actual = commandValidator.validate("create cd 12345678 1 0");
		assertFalse(actual);
	}

	@Test
	void cd_account_starting_balance_cannot_be_negative() {
		boolean actual = commandValidator.validate("create cd 12345678 1 -1");
		assertFalse(actual);
	}

	@Test
	void cd_account_starting_balance_accepts_minimum() {
		boolean actual = commandValidator.validate("create cd 12345678 1 1000");
		assertTrue(actual);
	}

	@Test
	void cd_account_starting_balance_accepts_maximum() {
		boolean actual = commandValidator.validate("create cd 12345678 1 10000");
		assertTrue(actual);
	}

	@Test
	void cd_account_starting_balance_rejects_above_upper_limit() {
		boolean actual = commandValidator.validate("create cd 12345678 1 10001");
		assertFalse(actual);
	}

	@Test
	void cd_account_starting_balance_rejects_below_lower_limit() {
		boolean actual = commandValidator.validate("create cd 12345678 1 999");
		assertFalse(actual);
	}

	@Test
	void cd_account_accepts_floating_point() {
		boolean actual = commandValidator.validate("create cd 12345678 1 9999.23");
		assertTrue(actual);
	}

	@Test
	void account_ID_is_not_unique() {
		bank.openCheckingAccount(ID, APR);
		boolean actual = commandValidator.validate("create savings 12345678 1");
		assertFalse(actual);
	}

	@Test
	void account_ID_is_unique() {
		boolean actual = commandValidator.validate("create savings 55555555 1");
		assertTrue(actual);
	}

}
