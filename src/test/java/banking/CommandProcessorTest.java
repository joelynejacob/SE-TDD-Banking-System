package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandProcessorTest {

	private final String ID_TEST = "12345678";
	private final double BALANCE_TEST = 100;
	private CommandProcessor commandProcessor;
	private Bank bank;

	@BeforeEach
	void setup() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);
		String inputString = "create checking 12345678 2.0";
		commandProcessor.execute(inputString);
	}

	@Test
	void account_is_created_using_command() {
		assertTrue(bank.accountExistsByQuickID(ID_TEST));
	}

	@Test
	void money_is_deposited_using_command() {
		String command = "deposit 12345678 100";
		commandProcessor.execute(command);
		assertEquals(BALANCE_TEST, bank.getAccountById(ID_TEST).getBalance());
	}
}
