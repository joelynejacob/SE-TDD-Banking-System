package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandValidatorTest {
	private final String ID = "12345678";
	private final double APR = 0.1;
	CommandValidator commandValidator;
	SavingsAccount savingsAccountTest;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		commandValidator = new CommandValidator(bank);
		savingsAccountTest = new SavingsAccount(ID, APR);

	}

	@Test
	void command_action_missing() {
		boolean actual = commandValidator.validate("savings 12345678 0.0");
		assertFalse(actual);
	}

	@Test
	void only_one_command_component() {
		boolean actual = commandValidator.validate("create");
		assertFalse(actual);
	}

	@Test
	void only_two_command_components() {
		boolean actual = commandValidator.validate("create savings");
		assertFalse(actual);
	}

	@Test
	void only_three_command_components() {
		boolean actual = commandValidator.validate("create savings 12345678");
		assertFalse(actual);
	}

	@Test
	void valid_command() {
		boolean actual = commandValidator.validate("create savings 12345678 0.0");
		assertTrue(actual);
	}

	@Test
	void command_action_not_valid() {
		boolean actual = commandValidator.validate("foobar savings 12345678 0.0");
		assertFalse(actual);
	}

	@Test
	void command_action_is_case_insensitive() {
		boolean actual = commandValidator.validate("create savings 12345678 0.0");
		assertTrue(actual);
	}

	@Test
	void command_action_does_not_accept_whitespace_before_it() {
		boolean actual = commandValidator.validate("    create savings 12345678 0.0");
		assertFalse(actual);
	}

	@Test
	void command_does_not_accept_whitespace_between_it() {
		boolean actual = commandValidator.validate("create     savings 12345678 0.0");
		assertFalse(actual);
	}

	@Test

	void command_accept_whitespace_at_end() {
		boolean actual = commandValidator.validate("create savings 12345678 0.0    ");
		assertTrue(actual);
	}

	@Test
	void command_action_only_has_chars() {
		boolean actual = commandValidator.validate("creat3 savings 12345678 0.0");
		assertFalse(actual);
	}

	@Test
	void ID_does_not_accept_less_than_8_digits() {
		boolean actual = commandValidator.validate("create checking 1278 0.0");
		assertFalse(actual);
	}

	@Test
	void ID_does_not_accept_non_decimal_digits() {
		boolean actual = commandValidator.validate("create checking 12WE56Io 0.0");
		assertFalse(actual);
	}

	@Test
	void ID_does_not_accept_negative_digits() {
		boolean actual = commandValidator.validate("create checking -12345678 0.0");
		assertFalse(actual);
	}

	@Test
	void ID_does_not_accept_float_digits() {
		boolean actual = commandValidator.validate("create checking 12345678.0 0.0");
		assertFalse(actual);
	}

	@Test
	void ID_accepts_lower_boundary() {
		boolean actual = commandValidator.validate("create checking 00000000 0.0");
		assertTrue(actual);
	}

	@Test
	void ID_rejects_below_lower_boundary() {
		boolean actual = commandValidator.validate("create checking -10000000 0.0");
		assertFalse(actual);
	}

	@Test
	void ID_accepts_upper_boundary() {
		boolean actual = commandValidator.validate("create checking 99999999 0.0");
		assertTrue(actual);
	}

	@Test
	void ID_rejects_after_upper_boundary() {
		boolean actual = commandValidator.validate("create checking 100000000 0.0");
		assertFalse(actual);
	}

}
