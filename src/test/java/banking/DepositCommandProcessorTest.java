package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepositCommandProcessorTest {
	private final double DEPOSIT_AMOUNT_TEST = 100;
	private final double DEPOSIT_AMOUNT_TEST_2 = 1000;
	private final double ACCOUNT_STARTING_BALANCE = 500;
	private final String ID_TEST = "12345678";
	private final String ID_TEST_2 = "87654321";
	private final double APR_TEST = 1.0;
	private final String TEST_STRING = "deposit 12345678 100";
	private final String TEST_STRING_2 = "deposit 87654321 1000";
	private CommandProcessor commandProcessor;
	private Bank bank;

	@BeforeEach
	void setup() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);
		bank.openCheckingAccount(ID_TEST, APR_TEST);
		bank.openSavingsAccount(ID_TEST_2, APR_TEST);

	}

	@Test
	void deposit_into_empty_checking_account() {
		commandProcessor.execute(TEST_STRING);
		assertEquals(DEPOSIT_AMOUNT_TEST, bank.getAccountById(ID_TEST).getBalance());
	}

	@Test
	void deposit_into_non_empty_checking_account() {
		bank.addMoneyThroughBank(ID_TEST, ACCOUNT_STARTING_BALANCE);
		commandProcessor.execute(TEST_STRING);
		assertEquals(ACCOUNT_STARTING_BALANCE + DEPOSIT_AMOUNT_TEST, bank.getAccounts().get(ID_TEST).getBalance());
	}

	@Test
	void can_deposit_into_empty_savings_account() {
		commandProcessor.execute(TEST_STRING_2);
		assertEquals(DEPOSIT_AMOUNT_TEST_2, bank.getAccounts().get(ID_TEST_2).getBalance());
	}

	@Test
	void deposit_into_non_empty_savings_account() {
		bank.addMoneyThroughBank(ID_TEST_2, ACCOUNT_STARTING_BALANCE);
		commandProcessor.execute(TEST_STRING_2);
		assertEquals(DEPOSIT_AMOUNT_TEST_2 + ACCOUNT_STARTING_BALANCE, bank.getAccounts().get(ID_TEST_2).getBalance());
	}

	@Test
	void deposit_into_checking_account_twice() {
		commandProcessor.execute(TEST_STRING);
		commandProcessor.execute(TEST_STRING);
		assertEquals(DEPOSIT_AMOUNT_TEST * 2, bank.getAccounts().get(ID_TEST).getBalance());
	}

}
