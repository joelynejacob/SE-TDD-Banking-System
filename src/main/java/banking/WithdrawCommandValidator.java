package banking;

public class WithdrawCommandValidator extends CommandValidator {

	private final double WITHDRAW_STRING_LENGTH = 3;

	public WithdrawCommandValidator(Bank bank) {
		super(bank);

	}

	public boolean validate(String[] inputString) {
		if (inputStringHasEnoughComponents(inputString) && isAccountIDValid(inputString[1])
				&& bank.accountExistsByQuickID(inputString[1]) && isAmountValid(inputString, false)
				&& IsWithdrawalTimeValid(inputString)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean IsWithdrawalTimeValid(String[] inputString) {
		String accountID = inputString[1];
		return bank.getAccountById(accountID).isWithdrawalTimeValid();
	}

	@Override
	public boolean inputStringHasEnoughComponents(String[] inputString) {
		if (inputString.length == WITHDRAW_STRING_LENGTH) {
			return true;
		} else {
			return false;
		}
	}
}