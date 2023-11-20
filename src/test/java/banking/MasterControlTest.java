package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MasterControlTest {
	private final String SAMPLE_VALID_CREATE_CHECKING_ACCOUNT_COMMAND = "create checking 12345678 1.0";
	private final String SAMPLE_VALID_CREATE_SAVINGS_ACCOUNT_COMMAND = "create savings 12345678 1.0";
	private final String SAMPLE_VALID_DEPOSIT_COMMAND = "deposit 12345678 100";
	private final String SAMPLE_VALID_WITHDRAW_COMMAND = "withdraw 12345678 100";

	MasterControl masterControl;
	private List<String> input;

	@BeforeEach
	void setUp() {
		input = new ArrayList<>();
		Bank bank = new Bank();
		CommandStorage commandStorage = new CommandStorage(bank);
		masterControl = new MasterControl(new CommandValidator(bank), new CommandProcessor(bank), commandStorage,
				new GetOutput(bank, commandStorage));
	}

	private void assertSingleCommand(String command, List<String> actual) {
		assertEquals(1, actual.size());
		assertEquals(command, actual.get(0));
	}

	@Test
	void typo_in_create_command_is_invalid() {
		input.add("creat checking 12345678 1.0");
		List<String> actual = masterControl.start(input);
		assertSingleCommand("creat checking 12345678 1.0", actual);
	}

	@Test
	void typo_in_deposit_command_is_invalid() {
		input.add("deposiy 12345678 200");

		List<String> actual = masterControl.start(input);
		assertSingleCommand("deposiy 12345678 200", actual);
	}

	@Test
	void two_commands_with_typos_invalid() {
		input.add("creat checking 12345678 1.0");
		input.add("deposiy 12345678 100");

		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertEquals("creat checking 12345678 1.0", actual.get(0));
		assertEquals("deposiy 12345678 100", actual.get(1));
	}

	@Test
	void creating_accounts_with_same_id_invalid() {
		input.add(SAMPLE_VALID_CREATE_CHECKING_ACCOUNT_COMMAND);
		input.add(SAMPLE_VALID_CREATE_CHECKING_ACCOUNT_COMMAND);

		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
		assertEquals(SAMPLE_VALID_CREATE_CHECKING_ACCOUNT_COMMAND, actual.get(1));

	}

	@Test
	void cannot_open_cd_account_without_balance() {
		input.add("create cd 12345678 0.01");
		assertSingleCommand("create cd 12345678 0.01", masterControl.start(input));
	}

	@Test
	void valid_withdraw_command_stored_and_works() {
		input.add("create checking 12345678 1");
		input.add(SAMPLE_VALID_DEPOSIT_COMMAND);
		input.add(SAMPLE_VALID_WITHDRAW_COMMAND);

		List<String> actual = masterControl.start(input);
		assertEquals(3, actual.size());
		assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
		assertEquals("Deposit 12345678 100", actual.get(1));
		assertEquals("Withdraw 12345678 100", actual.get(2));
	}

	@Test
	void cannot_withdraw_from_account_that_does_not_exist() {
		input.add(SAMPLE_VALID_WITHDRAW_COMMAND);

		assertSingleCommand(SAMPLE_VALID_WITHDRAW_COMMAND, masterControl.start(input));
	}

	@Test
	void cannot_withdraw_more_than_four_hundred_from_checking_accounts() {
		input.add(SAMPLE_VALID_CREATE_CHECKING_ACCOUNT_COMMAND);
		input.add("withdraw 12345678 401");

		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertEquals("withdraw 12345678 401", actual.get(1));
	}

	@Test
	void cannot_withdraw_more_than_thousand_dollars_from_savings() {
		input.add(SAMPLE_VALID_CREATE_SAVINGS_ACCOUNT_COMMAND);
		input.add("withdraw 12345678 1001");

		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertEquals("withdraw 12345678 1001", actual.get(1));
	}

	@Test
	void cannot_deposit_into_account_that_does_not_exist() {
		input.add(SAMPLE_VALID_DEPOSIT_COMMAND);

		assertSingleCommand(SAMPLE_VALID_DEPOSIT_COMMAND, masterControl.start(input));
	}

	@Test
	void valid_deposit_command_stored_and_works() {
		input.add("create checking 12345678 1");
		input.add(SAMPLE_VALID_DEPOSIT_COMMAND);

		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertEquals("Checking 12345678 100.00 1.00", actual.get(0));
		assertEquals("Deposit 12345678 100", actual.get(1));
	}

	@Test
	void cannot_deposit_into_cd_account() {
		input.add("create cd 12345678 1 1000");
		input.add(SAMPLE_VALID_DEPOSIT_COMMAND);

		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertEquals(SAMPLE_VALID_DEPOSIT_COMMAND, actual.get(1));
	}

	@Test
	void cannot_deposit_more_than_a_thousand_into_checking_accounts() {
		input.add(SAMPLE_VALID_CREATE_CHECKING_ACCOUNT_COMMAND);
		input.add("deposit 12345678 1001");

		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertEquals("deposit 12345678 1001", actual.get(1));
	}

	@Test
	void cannot_deposit_more_than_twenty_five_hundred_dollars_into_savings() {
		input.add(SAMPLE_VALID_CREATE_SAVINGS_ACCOUNT_COMMAND);
		input.add("deposit 12345678 2501");

		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertEquals("deposit 12345678 2501", actual.get(1));
	}

	@Test
	void cannot_transfer_between_the_same_account() {
		input.add(SAMPLE_VALID_CREATE_CHECKING_ACCOUNT_COMMAND);
		input.add("Create Checking 87654321 1.2");
		input.add("Transfer 12345678 12345678 100");

		List<String> actual = masterControl.start(input);
		assertEquals(3, actual.size());
		assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
		assertEquals("Checking 87654321 0.00 1.20", actual.get(1));
		assertEquals("Transfer 12345678 12345678 100", actual.get(2));

	}

	@Test
	void can_transfer_between_different_accounts() {
		input.add(SAMPLE_VALID_CREATE_CHECKING_ACCOUNT_COMMAND);
		input.add("Create Checking 87654321 1.2");
		input.add("Transfer 12345678 87654321 100");

		List<String> actual = masterControl.start(input);
		assertEquals(4, actual.size());
		assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
		assertEquals("Transfer 12345678 87654321 100", actual.get(1));
		assertEquals("Checking 87654321 0.00 1.20", actual.get(2));
		assertEquals("Transfer 12345678 87654321 100", actual.get(3));

	}

	@Test
	void cannot_create_an_account_that_is_not_an_eight_digit_number() {
		input.add("create checking 123456 0.01");
		assertSingleCommand("create checking 123456 0.01", masterControl.start(input));
	}

	@Test
	void invalid_commands_output_as_a_list() {
		input.add("im");
		input.add("tired");
		input.add("deposit 123456 1000");
		input.add("transfer 12345678 87654321 100");
		List<String> actual = masterControl.start(input);

		assertEquals("im", actual.get(0));
		assertEquals("tired", actual.get(1));
		assertEquals("deposit 123456 1000", actual.get(2));
		assertEquals("transfer 12345678 87654321 100", actual.get(3));

	}

	@Test
	void valid_create_command_creates_account_and_stores_transaction_history() {
		input.add("Create checking 12345678 1.245");
		input.add("depoSit 12345678 500");
		input.add("withdraw 12345678 100");
		input.add("pass 1");

		List<String> actual = masterControl.start(input);
		assertEquals(3, actual.size());
		assertEquals("Checking 12345678 400.41 1.24", actual.get(0));
		assertEquals("Deposit 12345678 500", actual.get(1));
		assertEquals("Withdraw 12345678 100", actual.get(2));
	}

	@Test
	void valid_create_command_creates_account_and_stores_transaction_history_with_invalid_command_at_end() {
		input.add("Create checking 12345678 1.245");
		input.add("depoSit 12345678 500");
		input.add("withdraw 12345678 100");
		input.add("pass 1");
		input.add("withdraw 12345678");

		List<String> actual = masterControl.start(input);
		assertEquals(4, actual.size());
		assertEquals("Checking 12345678 400.41 1.24", actual.get(0));
		assertEquals("Deposit 12345678 500", actual.get(1));
		assertEquals("Withdraw 12345678 100", actual.get(2));
		assertEquals("withdraw 12345678", actual.get(3));

	}

	@Test
	void valid_create_commands_create_two_accounts_and_stores_transaction_history_with_invalid_command_at_end() {
		input.add("Create checking 12345678 1.245");
		input.add("Create saviNgs 87654321 2.005");
		input.add("depoSit 12345678 500");
		input.add("withdraw 12345678 100");
		input.add("depoSit 87654321 1900");
		input.add("transfer 12345678 87654321 100");
		input.add("withdraw 12345678");

		List<String> actual = masterControl.start(input);
		assertEquals(8, actual.size());
		assertEquals("Checking 12345678 300.00 1.24", actual.get(0));
		assertEquals("Deposit 12345678 500", actual.get(1));
		assertEquals("Withdraw 12345678 100", actual.get(2));
		assertEquals("Transfer 12345678 87654321 100", actual.get(3));
		assertEquals("Savings 87654321 2000.00 2.00", actual.get(4));
		assertEquals("Deposit 87654321 1900", actual.get(5));
		assertEquals("Transfer 12345678 87654321 100", actual.get(6));
		assertEquals("withdraw 12345678", actual.get(7));
	}

	@Test
	void pass_time_empty_account_removed_and_no_transaction_history_displayed() {
		input.add("Create checking 12345678 1.245");
		input.add("Withdraw 12345678 100");
		input.add("pass 1");

		List<String> actual = masterControl.start(input);
		assertEquals(0, actual.size());
	}

	@Test
	void can_create_account_with_a_previously_closed_account_id() {
		input.add("Create checking 12345678 1.245");
		input.add("Deposit 12345678 0");
		input.add("Withdraw 12345678 100");
		input.add("Withdraw 12345678 0");
		input.add("pass 1");
		input.add("Create savings 12345678 2.00");

		List<String> actual = masterControl.start(input);
		assertEquals(1, actual.size());
		assertEquals("Savings 12345678 0.00 2.00", actual.get(0));
		System.out.println(actual);
	}

	@Test
	void display_transactions_of_only_new_account_if_made_with_previously_used_id() {
		input.add("Create checking 12345678 1.245");
		input.add("Deposit 12345678 0");
		input.add("Withdraw 12345678 100");
		input.add("Withdraw 12345678 0");
		input.add("pass 1");
		input.add("Create savings 12345678 2.00");
		input.add("Deposit 12345678 400");

		List<String> actual = masterControl.start(input);
		assertEquals(2, actual.size());
		assertEquals("Savings 12345678 400.00 2.00", actual.get(0));
		assertEquals("Deposit 12345678 400", actual.get(1));
	}

	@Test
	void display_transfer_transactions_of_removed_account_if_part_of_transfer() {
		input.add("Create checking 12345678 0.0");
		input.add("Deposit 12345678 0");
		input.add("Withdraw 12345678 100");
		input.add("Create checking 11111111 1.2");
		input.add("Deposit 11111111 100");
		input.add("Transfer 11111111 12345678 0");
		input.add("pass 1");

		List<String> actual = masterControl.start(input);
		assertEquals(3, actual.size());
		assertEquals("Checking 11111111 100.10 1.20", actual.get(0));
		assertEquals("Deposit 11111111 100", actual.get(1));
		assertEquals("Transfer 11111111 12345678 0", actual.get(2));
	}

	@Test
	void transfer_transaction_does_not_show_up_if_both_accounts_removed() {
		input.add("Create checking 12345678 0.0");
		input.add("Deposit 12345678 0");
		input.add("Create checking 11111111 1.2");
		input.add("Transfer 11111111 12345678 0");
		input.add("pass 1");

		List<String> actual = masterControl.start(input);
		assertEquals(0, actual.size());
		System.out.println(actual);
	}

	@Test
	void no_two_accounts_with_same_id_created() {
		input.add("Create checking 12345678 1.245");
		input.add("Deposit 12345678 100");
		input.add("Withdraw 12345678 50");
		input.add("Create savings 12345678 2.00");

		List<String> actual = masterControl.start(input);
		assertEquals(4, actual.size());
		assertEquals("Checking 12345678 50.00 1.24", actual.get(0));
		assertEquals("Deposit 12345678 100", actual.get(1));
		assertEquals("Withdraw 12345678 50", actual.get(2));
		assertEquals("Create savings 12345678 2.00", actual.get(3));
		System.out.println(actual);
	}

	@Test
	void transferring_between_same_account_is_invalid() {
		input.add("Create checking 12345678 1.245");
		input.add("Deposit 12345678 100");
		input.add("Transfer 12345678 12345678 0");
		input.add("Create savings 87654321 2.00");

		List<String> actual = masterControl.start(input);
		assertEquals(4, actual.size());
		assertEquals("Checking 12345678 100.00 1.24", actual.get(0));
		assertEquals("Deposit 12345678 100", actual.get(1));
		assertEquals("Savings 87654321 0.00 2.00", actual.get(2));
		assertEquals("Transfer 12345678 12345678 0", actual.get(3));

		System.out.println(actual);
	}

	@Test
	void savings_account_can_only_withdraw_once_per_month() {
		input.add("Create savings 12345678 0.6");
		input.add("Deposit 12345678 700");
		input.add("Withdraw 12345678 300");
		input.add("Withdraw 12345678 200");

		List<String> actual = masterControl.start(input);

		assertEquals(4, actual.size());
		assertEquals("Savings 12345678 400.00 0.60", actual.get(0));
		assertEquals("Deposit 12345678 700", actual.get(1));
		assertEquals("Withdraw 12345678 300", actual.get(2));
		assertEquals("Withdraw 12345678 200", actual.get(3));
	}

	@Test
	void cd_account_cannot_withdraw_before_one_year() {
		input.add("Create cd 87654321 2.1 2000");
		input.add("pass 1");
		input.add("Withdraw 12345678 300");
		List<String> actual = masterControl.start(input);

		assertEquals(2, actual.size());
		assertEquals("Cd 87654321 2014.03 2.10", actual.get(0));
		assertEquals("Withdraw 12345678 300", actual.get(1));
	}

	@Test
	void pass_time_with_multiple_accounts_present() {
		input.add("Create checking 12345678 1.245");
		input.add("Deposit 12345678 100");
		input.add("Create checking 11111111 1.5");
		input.add("Create cd 87654321 2.1 2000");
		input.add("pass 1");

		List<String> actual = masterControl.start(input);
		assertEquals(3, actual.size());
		assertEquals("Checking 12345678 100.10 1.24", actual.get(0));
		assertEquals("Deposit 12345678 100", actual.get(1));
		assertEquals("Cd 87654321 2014.03 2.10", actual.get(2));

		System.out.println(actual);
	}

	@Test
	void sample_make_sure_this_passes_unchanged_or_you_will_fail() {
		input.add("Create savings 12345678 0.6");
		input.add("Deposit 12345678 700");
		input.add("Deposit 12345678 5000");
		input.add("creAte cHecKing 98765432 0.01");
		input.add("Deposit 98765432 300");
		input.add("Transfer 98765432 12345678 300");
		input.add("Pass 1");
		input.add("Create cd 23456789 1.2 2000");
		List<String> actual = masterControl.start(input);

		assertEquals(5, actual.size());
		assertEquals("Savings 12345678 1000.50 0.60", actual.get(0));
		assertEquals("Deposit 12345678 700", actual.get(1));
		assertEquals("Transfer 98765432 12345678 300", actual.get(2));
		assertEquals("Cd 23456789 2000.00 1.20", actual.get(3));
		assertEquals("Deposit 12345678 5000", actual.get(4));
	}

}