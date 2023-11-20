package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassTimeCommandValidatorTest {
	CommandValidator commandValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		commandValidator = new CommandValidator(bank);

	}

	@Test
	void pass_time_command_is_valid() {
		boolean actual = commandValidator.validate("pass 1");
		assertTrue(actual);
	}

	@Test
	void pass_time_command_does_not_have_enough_components() {
		boolean actual = commandValidator.validate("pass");
		assertFalse(actual);
	}

	@Test
	void pass_time_command_has_extra_components() {
		boolean actual = commandValidator.validate("pass 2 4");
		assertFalse(actual);
	}

	@Test
	void pass_time_command_is_case_insensitive() {
		boolean actual = commandValidator.validate("PAss 4");
		assertTrue(actual);
	}

	@Test
	void pass_time_command_cannot_have_typos() {
		boolean actual = commandValidator.validate("pa$$ 4");
		assertFalse(actual);
	}

	@Test
	void pass_time_command_needs_to_exist() {
		boolean actual = commandValidator.validate("5");
		assertFalse(actual);
	}

	@Test
	void pass_time_command_cant_have_whitespace_before_it() {
		boolean actual = commandValidator.validate("   pass 4");
		assertFalse(actual);
	}

	@Test
	void pass_time_command_cant_have_whitespace_between() {
		boolean actual = commandValidator.validate("pass   4");
		assertFalse(actual);
	}

	@Test
	void pass_time_command_can_have_whitespace_at_end() {
		boolean actual = commandValidator.validate("pass 4   ");
		assertTrue(actual);
	}

	@Test
	void pass_time_cannot_be_more_than_max_value() {
		boolean actual = commandValidator.validate("pass 61");
		assertFalse(actual);
	}

	@Test
	void pass_time_can_be_max_value() {
		boolean actual = commandValidator.validate("pass 60");
		assertTrue(actual);
	}

	@Test
	void pass_time_can_be_less_than_max_value() {
		boolean actual = commandValidator.validate("pass 59");
		assertTrue(actual);
	}

	@Test
	void pass_time_cant_be_negative() {
		boolean actual = commandValidator.validate("pass -1");
		assertFalse(actual);
	}

	@Test
	void pass_time_cant_be_zero() {
		boolean actual = commandValidator.validate("pass 0");
		assertFalse(actual);
	}

	@Test
	void pass_time_can_be_more_than_min_value() {
		boolean actual = commandValidator.validate("pass 1");
		assertTrue(actual);
	}

	@Test
	void pass_time_has_to_be_int() {
		boolean actual = commandValidator.validate("pass 3.4");
		assertFalse(actual);
	}

}
