package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateCommandProcessorTest {
	private static final double STARTING_BALANCE_TEST = 500;
	private final String ACCOUNT_TYPE_TEST_3 = "Savings";
	private final String ACCOUNT_TYPE_TEST_2 = "Cd";
	private final String ID_TEST = "12345678";
	private final String ID_TEST_2 = "87654321";
	private final String ID_TEST_3 = "11111111";
	private final Double APR_TEST = 1.0;
	private final String ACCOUNT_TYPE_TEST = "Checking";

	private CommandProcessor commandProcessor;
	private Bank bank;

	@BeforeEach
	void setup() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);
		String inputString = "create checking 12345678 1.0";
		String inputString2 = "create cd 87654321 1.0 500";
		String inputString3 = "create savings 11111111 1.0";
		commandProcessor.execute(inputString);
		commandProcessor.execute(inputString2);
		commandProcessor.execute(inputString3);
	}

	@Test
	void can_create_checking_account() {
		assertTrue(bank.accountExistsByQuickID(ID_TEST));
	}

	@Test
	void can_create_savings_account() {
		assertTrue(bank.accountExistsByQuickID(ID_TEST_3));
	}

	@Test
	void can_create_cd_account() {
		assertTrue(bank.accountExistsByQuickID(ID_TEST_2));
	}

	@Test
	void created_checking_account_has_supplied_APR() {
		assertEquals(APR_TEST, bank.getAccounts().get(ID_TEST).getApr());
	}

	@Test
	void created_checking_account_has_supplied_ID() {
		assertEquals(ID_TEST, bank.getAccounts().get(ID_TEST).getID());
	}

	@Test
	void created_checking_account_has_correct_account_type() {
		assertEquals(ACCOUNT_TYPE_TEST, bank.getAccounts().get(ID_TEST).getAccountType());
	}

	@Test
	void created_cd_account_has_correct_balance() {
		assertEquals(STARTING_BALANCE_TEST, bank.getAccounts().get(ID_TEST_2).getBalance());
	}

	@Test
	void created_cd_account_has_correct_account_type() {
		assertEquals(ACCOUNT_TYPE_TEST_2, bank.getAccounts().get(ID_TEST_2).getAccountType());
	}

	@Test
	void created_savings_account_has_correct_account_type() {
		assertEquals(ACCOUNT_TYPE_TEST_3, bank.getAccounts().get(ID_TEST_3).getAccountType());
	}

	@Test
	void bank_contains_created_accounts() {
		assertEquals(3, bank.getAccounts().size());
	}
}
