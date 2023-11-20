package banking;

public class DepositCommandValidator extends CommandValidator {

	private final double DEPOSIT_STRING_LENGTH = 3;

	public DepositCommandValidator(Bank bank) {
		super(bank);

	}

	public boolean validate(String[] inputString) {
		if (inputStringHasEnoughComponents(inputString) && isAccountIDValid(inputString[1])
				&& bank.accountExistsByQuickID(inputString[1]) && isAmountValid(inputString, true)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean inputStringHasEnoughComponents(String[] inputString) {
		if (inputString.length == DEPOSIT_STRING_LENGTH) {
			return true;
		} else {
			return false;
		}
	}
}