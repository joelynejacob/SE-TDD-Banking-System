package banking;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassTimeCommandProcessorTest {
	private final String ID = "12345678";
	private final String ID_2 = "11111111";
	private final String ID_3 = "22222222";
	private final String ID_4 = "33333333";
	private final double APR = 2;
	private final double BALANCE = 1000;
	private final String TEST_STRING = "pass 1";
	private final String TEST_STRING_2 = "pass 2";
	CommandProcessor commandProcessor;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);
		bank.openCheckingAccount(ID, APR);
		bank.openSavingsAccount(ID_2, APR);
		bank.openCheckingAccount(ID_3, APR);
		bank.openCDAccount(ID_4, APR, BALANCE);

	}

	@Test
	void passing_one_month_calculates_correct_new_balance() {
		bank.addMoneyThroughBank(ID, 500);
		commandProcessor.execute(TEST_STRING);
		double actual = ((0.02 / 12 * 500) + 500);
		assertEquals(actual, bank.getAccountById(ID).getBalance());
	}

	@Test
	void passing_two_months_calculate_correct_new_balance() {
		bank.addMoneyThroughBank(ID, 500);
		commandProcessor.execute(TEST_STRING_2);
		double firstMonthBalance = ((0.02 / 12 * 500) + 500);
		double actual = (0.02 / 12 * firstMonthBalance) + firstMonthBalance;
		assertEquals(actual, bank.getAccountById(ID).getBalance());
	}

	@Test
	void passing_one_month_removes_one_empty_account() {
		bank.addMoneyThroughBank(ID, 500);
		bank.addMoneyThroughBank(ID_2, 500);
		commandProcessor.execute(TEST_STRING);
		assertFalse(bank.accountExistsByQuickID(ID_3));
	}

	@Test
	void passing_one_month_removes_two_empty_accounts() {
		bank.addMoneyThroughBank(ID, 500);
		commandProcessor.execute(TEST_STRING);
		assertFalse(bank.accountExistsByQuickID(ID_3));
		assertFalse(bank.accountExistsByQuickID(ID_2));
	}

	@Test
	void passing_two_months_removes_one_empty_account() {
		bank.addMoneyThroughBank(ID, 500);
		bank.addMoneyThroughBank(ID_2, 500);
		commandProcessor.execute(TEST_STRING_2);
		assertFalse(bank.accountExistsByQuickID(ID_3));
	}

	@Test
	void passing_two_months_removes_two_empty_accounts() {
		bank.addMoneyThroughBank(ID, 500);
		commandProcessor.execute(TEST_STRING_2);
		assertFalse(bank.accountExistsByQuickID(ID_3));
		assertFalse(bank.accountExistsByQuickID(ID_2));
	}

	@Test
	void passing_one_month_does_not_remove_non_empty_account() {
		bank.addMoneyThroughBank(ID, 500);
		commandProcessor.execute(TEST_STRING);
		assertTrue(bank.accountExistsByQuickID(ID));
	}

	@Test
	void passing_one_month_deducts_twenty_five_from_balance_under_hundred() {
		bank.addMoneyThroughBank(ID, 90);
		commandProcessor.execute(TEST_STRING);
		double actual = (0.02 / 12 * 65) + 65;
		assertEquals(actual, bank.getAccountById(ID).getBalance());
	}

	@Test
	void passing_two_months_deducts_fifty_from_balance_under_hundred() {
		bank.addMoneyThroughBank(ID, 90);
		commandProcessor.execute(TEST_STRING_2);
		double firstMonthBalance = (0.02 / 12 * 65) + 65;
		double secondMonthMinBalanceFeeDeducted = firstMonthBalance - 25;
		double actual = (0.02 / 12 * secondMonthMinBalanceFeeDeducted) + secondMonthMinBalanceFeeDeducted;
		assertEquals(actual, bank.getAccountById(ID).getBalance());
	}

	@Test
	void cd_account_balance_is_calculated_correctly_after_one_month() {
		commandProcessor.execute(TEST_STRING);
		double calculatedBalance = BALANCE;
		for (int count = 0; count < 4; count++) {
			calculatedBalance = calculatedBalance + (calculatedBalance * 0.02 / 12);
		}
		assertEquals(calculatedBalance, bank.getAccountById(ID_4).getBalance());

	}

	@Test
	void cd_account_balance_is_calculated_correctly_after_two_months() {
		commandProcessor.execute(TEST_STRING_2);
		double calculatedBalance = BALANCE;
		for (int count = 0; count < 8; count++) {
			calculatedBalance = calculatedBalance + (calculatedBalance * 0.02 / 12);
		}
		assertEquals(calculatedBalance, bank.getAccountById(ID_4).getBalance());

	}

	@Test
	void pass_time_twice() {
		bank.addMoneyThroughBank(ID, 500);
		commandProcessor.execute(TEST_STRING_2);
		double firstMonthBalance = ((0.02 / 12 * 500) + 500);
		double secondMonthBalance = (0.02 / 12 * firstMonthBalance) + firstMonthBalance;
		commandProcessor.execute(TEST_STRING);
		double actual = (0.02 / 12 * secondMonthBalance) + secondMonthBalance;
		assertEquals(actual, bank.getAccountById(ID).getBalance());
	}
}