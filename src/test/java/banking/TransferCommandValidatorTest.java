package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferCommandValidatorTest {
	private final String ID = "12345678";
	private final String ID_2 = "11111111";
	private final String ID_3 = "22222222";
	private final String ID_4 = "33333333";
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
		bank.openCheckingAccount(ID_3, APR);
		bank.openSavingsAccount(ID_4, APR);

	}

	@Test
	void transfer_command_is_valid() {
		boolean actual = commandValidator.validate("transfer 12345678 11111111 200");
		assertTrue(actual);
	}

	@Test
	void transfer_command_does_not_have_enough_components() {
		boolean actual = commandValidator.validate("transfer 12345678 11111111");
		assertFalse(actual);
	}

	@Test
	void transfer_command_has_extra_components() {
		boolean actual = commandValidator.validate("transfer 12345678 11111111 3 200");
		assertFalse(actual);
	}

	@Test
	void transfer_command_is_case_insensitive() {
		boolean actual = commandValidator.validate("transfER 12345678 11111111 200");
		assertTrue(actual);
	}

	@Test
	void transfer_command_cannot_have_typos() {
		boolean actual = commandValidator.validate("transf3r 12345678 11111111 200");
		assertFalse(actual);
	}

	@Test
	void transfer_command_needs_to_exist() {
		boolean actual = commandValidator.validate("12345678 11111111 200");
		assertFalse(actual);
	}

	@Test
	void transfer_command_cant_have_whitespace_before_it() {
		boolean actual = commandValidator.validate("      transfer 12345678 11111111 200");
		assertFalse(actual);
	}

	@Test
	void transfer_command_cant_have_whitespace_between() {
		boolean actual = commandValidator.validate("transfer      12345678 11111111 200");
		assertFalse(actual);
	}

	@Test
	void transfer_amount_cant_be_negative() {
		boolean actual = commandValidator.validate("transfer 12345678 11111111 -1");
		assertFalse(actual);
	}

	@Test
	void transfer_amount_can_be_zero() {
		boolean actual = commandValidator.validate("transfer 12345678 11111111 0");
		assertTrue(actual);
	}

	@Test
	void transfer_amount_must_be_digits() {
		boolean actual = commandValidator.validate("transfer 12345678 11111111 hundred");
		assertFalse(actual);
	}

	@Test
	void cd_cannot_transfer_to() {
		bank.openCDAccount("87654321", APR, BALANCE);
		boolean actual = commandValidator.validate("transfer 12345678 87654321 200");
		assertFalse(actual);
	}

	@Test
	void cd_cannot_transfer_from() {
		bank.openCDAccount("87654321", APR, BALANCE);
		boolean actual = commandValidator.validate("transfer 87654321 12345678 200");
		assertFalse(actual);
	}

	@Test
	void cant_transfer_without_adding_account_in_bank() {
		boolean actual = commandValidator.validate("transfer 87654321 12345678 200");
		assertFalse(actual);
	}

	@Test
	void account_ID_of_sending_account_should_be_valid() {
		boolean actual = commandValidator.validate("transfer 1345678 11111111 0");
		assertFalse(actual);
	}

	@Test
	void account_ID_of_receiving_account_should_be_valid() {
		boolean actual = commandValidator.validate("transfer 12345678 1 0");
		assertFalse(actual);
	}

	@Test
	void can_transfer_between_checking_accounts() {
		boolean actual = commandValidator.validate("transfer 12345678 22222222 200");
		assertTrue(actual);
	}

	@Test
	void can_transfer_between_savings_accounts() {
		boolean actual = commandValidator.validate("transfer 11111111 33333333 200");
		assertTrue(actual);
	}

	@Test
	void can_transfer_between_saving_and_checking_accounts() {
		boolean actual = commandValidator.validate("transfer 12345678 33333333 200");
		assertTrue(actual);
	}

	@Test
	void cant_transfer_between_same_account() {
		boolean actual = commandValidator.validate("transfer 22222222 22222222 200");
		assertFalse(actual);
	}

	@Test
	void transfer_should_follow_withdraw_rule_of_sending_account() {
		boolean actual = commandValidator.validate("transfer 11111111 33333333 1500");
		assertFalse(actual);
	}

	@Test
	void transfer_should_follow_deposit_rule_of_receiving_account() {
		boolean actual = commandValidator.validate("transfer 11111111 22222222 1500");
		assertFalse(actual);
	}
}