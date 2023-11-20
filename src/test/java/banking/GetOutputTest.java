package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetOutputTest {
	private final String TEST_ACCOUNT_OUTPUT_STATE = "Savings 12345678 500.23 2.01";
	private final String TEST_ACCOUNT_OUTPUT_STATE_2 = "Checking 87654321 500.23 2.01";
	private final double APR = 2.0123;
	private final String TEST_ID = "12345678";
	private final String TEST_ID_2 = "87654321";
	private final String VALID_COMMAND = "create checking 12345678";
	private final String VALID_COMMAND_2 = "Deposit 12345678 500";
	private final String VALID_COMMAND_5 = "Deposit 87654321 500";
	private final String VALID_COMMAND_CASE_INSENSITIVE_TEST = "DEpOsiT 87654321 500";
	private final String INVALID_COMMAND = "blah";
	private final String INVALID_COMMAND_2 = "create";
	private final double TEST_AMOUNT = 500.2345;

	List<String> TestCompleteOutput = new ArrayList<>();
	private Bank bank;
	private CommandStorage commandStorage;
	private GetOutput getOutput;

	@BeforeEach
	void setup() {
		bank = new Bank();
		commandStorage = new CommandStorage(bank);
		getOutput = new GetOutput(bank, commandStorage);
	}

	@Test
	void output_is_empty_initially() {
		assertTrue(getOutput.outputResult().isEmpty());
	}

	@Test
	void correct_account_state_is_output() {
		bank.openSavingsAccount(TEST_ID, APR);
		bank.addMoneyThroughBank(TEST_ID, TEST_AMOUNT);
		assertEquals(TEST_ACCOUNT_OUTPUT_STATE, getOutput.outputResult().get(0));
	}

	@Test
	void can_output_two_account_states() {
		bank.openSavingsAccount(TEST_ID, APR);
		bank.openCheckingAccount(TEST_ID_2, APR);
		bank.addMoneyThroughBank(TEST_ID, TEST_AMOUNT);
		bank.addMoneyThroughBank(TEST_ID_2, TEST_AMOUNT);
		TestCompleteOutput.add(TEST_ACCOUNT_OUTPUT_STATE);
		TestCompleteOutput.add(TEST_ACCOUNT_OUTPUT_STATE_2);
		assertEquals(TestCompleteOutput, getOutput.outputResult());
		System.out.println(getOutput.outputResult());
	}

	@Test
	void valid_create_commands_not_part_of_transaction_history() {
		bank.openSavingsAccount(TEST_ID, APR);
		bank.addMoneyThroughBank(TEST_ID, TEST_AMOUNT);
		commandStorage.addValidCommand(VALID_COMMAND);

		TestCompleteOutput.add(TEST_ACCOUNT_OUTPUT_STATE);
		assertEquals(TestCompleteOutput, getOutput.outputResult());
	}

	@Test
	void valid_command_first_word_formatted() {
		bank.openSavingsAccount(TEST_ID_2, APR);
		bank.addMoneyThroughBank(TEST_ID_2, TEST_AMOUNT);
		commandStorage.addValidCommand(VALID_COMMAND_CASE_INSENSITIVE_TEST);

		assertEquals("Deposit 87654321 500", getOutput.outputResult().get(1));

	}

	@Test
	void account_state_is_correct_with_one_transaction_history() {
		bank.openSavingsAccount(TEST_ID, APR);
		bank.addMoneyThroughBank(TEST_ID, TEST_AMOUNT);
		commandStorage.addValidCommand(VALID_COMMAND_2);

		TestCompleteOutput.add(TEST_ACCOUNT_OUTPUT_STATE);
		TestCompleteOutput.add(VALID_COMMAND_2);
		assertEquals(TestCompleteOutput, getOutput.outputResult());
		System.out.println(getOutput.outputResult());

	}

	@Test
	void one_account_state_is_correct_with_two_transaction_histories() {
		bank.openSavingsAccount(TEST_ID, APR);
		bank.addMoneyThroughBank(TEST_ID, TEST_AMOUNT);

		commandStorage.addValidCommand(VALID_COMMAND_2);
		commandStorage.addValidCommand(VALID_COMMAND_2);

		TestCompleteOutput.add(TEST_ACCOUNT_OUTPUT_STATE);
		TestCompleteOutput.add(VALID_COMMAND_2);
		TestCompleteOutput.add(VALID_COMMAND_2);

		assertEquals(TestCompleteOutput, getOutput.outputResult());
		System.out.println(getOutput.outputResult());
	}

	@Test
	void output_two_account_states_with_transaction_histories() {
		bank.openSavingsAccount(TEST_ID, APR);
		bank.openCheckingAccount(TEST_ID_2, APR);

		bank.addMoneyThroughBank(TEST_ID, TEST_AMOUNT);
		commandStorage.addValidCommand(VALID_COMMAND_2);
		bank.addMoneyThroughBank(TEST_ID_2, TEST_AMOUNT);
		commandStorage.addValidCommand(VALID_COMMAND_5);

		TestCompleteOutput.add(TEST_ACCOUNT_OUTPUT_STATE);
		TestCompleteOutput.add(VALID_COMMAND_2);
		TestCompleteOutput.add(TEST_ACCOUNT_OUTPUT_STATE_2);
		TestCompleteOutput.add(VALID_COMMAND_5);

		assertEquals(TestCompleteOutput, getOutput.outputResult());
	}

	@Test
	void output_account_state_transaction_history_and_invalid_commands() {
		bank.openSavingsAccount(TEST_ID, APR);
		bank.addMoneyThroughBank(TEST_ID, TEST_AMOUNT);

		commandStorage.addValidCommand(VALID_COMMAND_2);
		commandStorage.addValidCommand(VALID_COMMAND_2);
		commandStorage.addInvalidCommand(INVALID_COMMAND);
		commandStorage.addInvalidCommand(INVALID_COMMAND_2);

		TestCompleteOutput.add(TEST_ACCOUNT_OUTPUT_STATE);
		TestCompleteOutput.add(VALID_COMMAND_2);
		TestCompleteOutput.add(VALID_COMMAND_2);
		TestCompleteOutput.add(INVALID_COMMAND);
		TestCompleteOutput.add(INVALID_COMMAND_2);

		assertEquals(TestCompleteOutput, getOutput.outputResult());
		System.out.println(getOutput.outputResult());

	}
}
