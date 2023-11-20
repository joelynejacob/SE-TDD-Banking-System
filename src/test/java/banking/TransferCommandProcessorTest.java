package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferCommandProcessorTest {
	private final String ID = "12345678";
	private final String ID_2 = "11111111";
	private final String ID_3 = "22222222";
	private final String ID_4 = "33333333";
	private final double APR = 2;
	private final double TEST_TRANSFER = 200;
	private final double TEST_TRANSFER_2 = 300;
	private final double TEST_TRANSFER_3 = 500;

	CommandProcessor commandProcessor;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);
		bank.openCheckingAccount(ID, APR);
		bank.openSavingsAccount(ID_2, APR);
		bank.openCheckingAccount(ID_3, APR);
		bank.openSavingsAccount(ID_4, APR);

	}

	@Test
	void can_transfer_between_checking_accounts() {
		bank.addMoneyThroughBank(ID, TEST_TRANSFER_3);
		commandProcessor.execute("transfer 12345678 22222222 200");
		assertEquals(TEST_TRANSFER, bank.getAccountById(ID_3).getBalance());
		assertEquals(TEST_TRANSFER_2, bank.getAccountById(ID).getBalance());
	}

	@Test
	void can_transfer_between_savings_accounts() {
		bank.addMoneyThroughBank(ID_2, TEST_TRANSFER_3);
		commandProcessor.execute("transfer 11111111 33333333 200");
		assertEquals(TEST_TRANSFER, bank.getAccountById(ID_4).getBalance());
		assertEquals(TEST_TRANSFER_2, bank.getAccountById(ID_2).getBalance());
	}

	@Test
	void can_transfer_from_savings_to_checking_accounts() {
		bank.addMoneyThroughBank(ID_2, TEST_TRANSFER_3);
		commandProcessor.execute("transfer 11111111 12345678 200");
		assertEquals(TEST_TRANSFER, bank.getAccountById(ID).getBalance());
		assertEquals(TEST_TRANSFER_2, bank.getAccountById(ID_2).getBalance());
	}

	@Test
	void can_transfer_from_checking_to_savings_accounts() {
		bank.addMoneyThroughBank(ID, TEST_TRANSFER_3);
		commandProcessor.execute("transfer 12345678 11111111 200");
		assertEquals(TEST_TRANSFER, bank.getAccountById(ID_2).getBalance());
		assertEquals(TEST_TRANSFER_2, bank.getAccountById(ID).getBalance());
	}

	@Test
	void can_transfer_amount_greater_than_balance() {
		bank.addMoneyThroughBank(ID, TEST_TRANSFER_3);
		commandProcessor.execute("transfer 12345678 11111111 600");
		assertEquals(TEST_TRANSFER_3, bank.getAccountById(ID_2).getBalance());
		assertEquals(0, bank.getAccountById(ID).getBalance());
	}

}