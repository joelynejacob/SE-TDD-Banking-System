package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandStorageTest {
	private final double APR = 2.0123;
	private final String TEST_ID = "12345678";
	private final String TEST_ID_2 = "87654321";
	private final String VALID_COMMAND = "create checking 12345678";
	private final String VALID_COMMAND_2 = "Deposit 12345678 500";
	private final String VALID_COMMAND_4 = "Transfer 12345678 87654321 200";
	private final String VALID_COMMAND_5 = "Deposit 87654321 500";

	private final String INVALID_COMMAND = "blah";
	private final String INVALID_COMMAND_2 = "create";

	public CommandStorage commandStorage;
	List<String> TestInvalidCommandsList = new ArrayList<>();
	List<String> TestValidCommandList = new ArrayList<>();
	Bank bank;
	List<String> TestValidCommandList2 = new ArrayList<>();

	@BeforeEach
	void setup() {
		bank = new Bank();
		commandStorage = new CommandStorage(bank);

	}

	@Test
	void invalid_commands_list_is_empty_at_start() {
		int actual = 0;
		assertEquals(actual, commandStorage.returnInvalidCommands().size());
	}

	@Test
	public void invalid_command_is_stored() {
		commandStorage.addInvalidCommand(INVALID_COMMAND);
		assertEquals(INVALID_COMMAND, commandStorage.returnInvalidCommands().get(0));
	}

	@Test
	void size_of_invalid_command_list_is_one_when_one_command_added() {
		commandStorage.addInvalidCommand(INVALID_COMMAND);
		int actual = 1;
		assertEquals(actual, commandStorage.returnInvalidCommands().size());
	}

	@Test
	public void can_add_two_invalid_commands_to_storage() {
		commandStorage.addInvalidCommand(INVALID_COMMAND);
		commandStorage.addInvalidCommand(INVALID_COMMAND_2);
		TestInvalidCommandsList.add(INVALID_COMMAND);
		TestInvalidCommandsList.add(INVALID_COMMAND_2);
		assertEquals(TestInvalidCommandsList, commandStorage.returnInvalidCommands());
	}

	@Test
	void size_of_invalid_command_list_is_two_when_two_commands_added() {
		commandStorage.addInvalidCommand(INVALID_COMMAND);
		commandStorage.addInvalidCommand(INVALID_COMMAND_2);
		int actual = 2;
		assertEquals(actual, commandStorage.returnInvalidCommands().size());

	}

	@Test
	void valid_command_list_is_empty_at_start() {
		assertTrue(commandStorage.getValidCommands().isEmpty());
	}

	@Test
	void valid_command_can_be_stored_for_one_id() {
		bank.openSavingsAccount(TEST_ID, APR);
		commandStorage.addValidCommand(VALID_COMMAND_2);
		TestValidCommandList.add(VALID_COMMAND_2);
		assertEquals(TestValidCommandList, commandStorage.getValidCommands().get(TEST_ID));
	}

	@Test
	void two_valid_commands_are_stored_for_an_id() {
		bank.openSavingsAccount(TEST_ID, APR);
		commandStorage.addValidCommand(VALID_COMMAND_2);
		commandStorage.addValidCommand(VALID_COMMAND_2);
		TestValidCommandList.add(VALID_COMMAND_2);
		TestValidCommandList.add(VALID_COMMAND_2);
		assertEquals(TestValidCommandList, commandStorage.getValidCommands().get(TEST_ID));
	}

	@Test
	void valid_commands_stored_for_two_ids() {
		bank.openSavingsAccount(TEST_ID, APR);
		bank.openCheckingAccount(TEST_ID_2, APR);
		commandStorage.addValidCommand(VALID_COMMAND_2);
		commandStorage.addValidCommand(VALID_COMMAND_5);
		assertEquals(VALID_COMMAND_2, commandStorage.getValidCommands().get(TEST_ID).get(0));
		assertEquals(VALID_COMMAND_5, commandStorage.getValidCommands().get(TEST_ID_2).get(0));
	}

	@Test
	void two_valid_commands_stored_for_two_ids() {
		bank.openSavingsAccount(TEST_ID, APR);
		bank.openCheckingAccount(TEST_ID_2, APR);
		commandStorage.addValidCommand(VALID_COMMAND_2);
		commandStorage.addValidCommand(VALID_COMMAND_5);
		commandStorage.addValidCommand(VALID_COMMAND_4);
		TestValidCommandList.add(VALID_COMMAND_2);
		TestValidCommandList.add(VALID_COMMAND_4);
		TestValidCommandList2.add(VALID_COMMAND_5);
		TestValidCommandList2.add(VALID_COMMAND_4);
		assertEquals(TestValidCommandList, commandStorage.getValidCommands().get(TEST_ID));
		assertEquals(TestValidCommandList2, commandStorage.getValidCommands().get(TEST_ID_2));
	}

	@Test
	void create_commands_not_added_to_valid_commands() {
		bank.openSavingsAccount(TEST_ID, APR);
		commandStorage.addValidCommand(VALID_COMMAND);

		assertTrue(commandStorage.getValidCommands().isEmpty());
	}

	@Test
	void transfer_commands_added_to_both_accounts_transaction_history() {
		bank.openSavingsAccount(TEST_ID, APR);
		bank.openCheckingAccount(TEST_ID_2, APR);
		commandStorage.addValidCommand(VALID_COMMAND_4);
		assertEquals(VALID_COMMAND_4, commandStorage.getValidCommands().get(TEST_ID).get(0));
		assertEquals(VALID_COMMAND_4, commandStorage.getValidCommands().get(TEST_ID_2).get(0));
	}

}
