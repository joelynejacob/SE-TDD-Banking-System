package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawCommandProcessorTest {
	private final String ID_TEST_3 = "11111111";
	private final double ACCOUNT_STARTING_BALANCE = 1000;
	private final String ID_TEST = "12345678";
	private final String ID_TEST_2 = "87654321";
	private final double APR_TEST = 1.0;
	private final String WITHDRAW_COMMAND_TEST = "withdraw 12345678 100";
	private final String WITHDRAW_COMMAND_TEST_2 = "withdraw 87654321 100";
	private final double STARTING_AMOUNT_TEST = 500;
	private final double WITHDRAW_AMOUNT_TEST = 100;

	private CommandProcessor commandProcessor;
	private Bank bank;

	@BeforeEach
	void setup() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);
		bank.openCheckingAccount(ID_TEST, APR_TEST);
		bank.openSavingsAccount(ID_TEST_2, APR_TEST);
		bank.openCDAccount(ID_TEST_3, APR_TEST, ACCOUNT_STARTING_BALANCE);

	}

	@Test
	void withdraw_from_empty_checking_account() {
		commandProcessor.execute(WITHDRAW_COMMAND_TEST);
		assertEquals(0, bank.getAccountById(ID_TEST).getBalance());
	}

	@Test
	void withdraw_from_empty_savings_account() {
		commandProcessor.execute(WITHDRAW_COMMAND_TEST_2);
		assertEquals(0, bank.getAccountById(ID_TEST).getBalance());
	}

	@Test
	void withdraw_once_from_checking_account() {
		bank.addMoneyThroughBank(ID_TEST, STARTING_AMOUNT_TEST);
		commandProcessor.execute(WITHDRAW_COMMAND_TEST);
		assertEquals(STARTING_AMOUNT_TEST - WITHDRAW_AMOUNT_TEST, bank.getAccountById(ID_TEST).getBalance());
	}

	@Test
	void withdraw_twice_from_checking_account() {
		bank.addMoneyThroughBank(ID_TEST, STARTING_AMOUNT_TEST);
		commandProcessor.execute(WITHDRAW_COMMAND_TEST);
		commandProcessor.execute(WITHDRAW_COMMAND_TEST);
		assertEquals(STARTING_AMOUNT_TEST - WITHDRAW_AMOUNT_TEST * 2, bank.getAccountById(ID_TEST).getBalance());
	}

	@Test
	void withdraw_once_from_savings_account() {
		bank.addMoneyThroughBank(ID_TEST_2, STARTING_AMOUNT_TEST);
		commandProcessor.execute(WITHDRAW_COMMAND_TEST_2);
		assertEquals(STARTING_AMOUNT_TEST - WITHDRAW_AMOUNT_TEST, bank.getAccountById(ID_TEST_2).getBalance());
	}

	@Test
	void withdraw_from_cd_account() {
		String inputString = "withdraw 11111111 1000";
		commandProcessor.execute(inputString);
		assertEquals(0, bank.getAccountById(ID_TEST_2).getBalance());
	}

}
