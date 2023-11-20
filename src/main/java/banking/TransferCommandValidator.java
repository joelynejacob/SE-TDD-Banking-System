package banking;

public class TransferCommandValidator extends CommandValidator {

	private final double DEPOSIT_STRING_LENGTH = 4;

	public TransferCommandValidator(Bank bank) {
		super(bank);

	}

	public boolean validate(String[] inputString) {
		return inputStringHasEnoughComponents(inputString) && AreBothAccountIDsValid(inputString)
				&& BothAccountsExist(inputString) && IsTransferAmountValid(inputString)
				&& AreTransferAccountTypesValid(inputString) && TransferIDsNotSame(inputString);
	}

	private boolean AreBothAccountIDsValid(String[] inputString) {
		if (isAccountIDValid(inputString[1]) && isAccountIDValid(inputString[2])) {
			return true;
		} else {
			return false;
		}

	}

	private boolean TransferIDsNotSame(String[] inputString) {
		if (inputString[1].equals(inputString[2])) {
			return false;
		} else {
			return true;
		}

	}

	private boolean BothAccountsExist(String[] inputString) {
		if (bank.accountExistsByQuickID(inputString[1]) && bank.accountExistsByQuickID(inputString[2])) {
			return true;
		} else {
			return false;
		}

	}

	private boolean AreTransferAccountTypesValid(String[] inputString) {
		if ((bank.getAccountById(inputString[1]).getAccountType() == "Checking"
				|| bank.getAccountById(inputString[1]).getAccountType() == "Savings")
				&& (bank.getAccountById(inputString[2]).getAccountType() == "Checking"
						|| bank.getAccountById(inputString[2]).getAccountType() == "Savings")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean IsTransferAmountValid(String[] inputString) {
		try {
			String transferAmount = inputString[3];
			double numTransfer = Double.parseDouble(transferAmount);
			if (numTransfer >= 0 && bank.getAccountById(inputString[1]).isWithdrawalAmountValid(numTransfer)
					&& bank.getAccountById(inputString[2]).isDepositAmountValid(numTransfer)) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
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