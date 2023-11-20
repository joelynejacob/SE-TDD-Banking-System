package banking;

public class CreateCommandValidator extends CommandValidator {

	private final int CD_COMMAND_LENGTH = 5;
	private final double CD_MAX_BALANCE = 10000;
	private final double CD_MIN_BALANCE = 1000;
	private final double MAX_APR = 10.0;
	private final double MIN_APR = 0.0;

	public CreateCommandValidator(Bank bank) {
		super(bank);
	}

	public boolean validate(String[] inputString) {
		if (inputStringHasEnoughComponents(inputString) && isAccountTypeValid(inputString[1])
				&& isAccountIDValid(inputString[2]) && isAccountAPRValid(inputString[3])) {
			if (inputString[1].equalsIgnoreCase("cd")) {
				return IsCdAccountValid(inputString);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isAccountIDValid(String idString) {
		boolean isParentValid = super.isAccountIDValid(idString);
		if (isParentValid && bank.accountExistsByQuickID(idString) == false) {
			return true;
		} else {
			return false;
		}
	}

	private boolean IsCdAccountValid(String[] inputString) {
		if (inputString.length != CD_COMMAND_LENGTH) {
			return false;
		} else {
			String startingBalance = inputString[4];
			return IsCdAccountStartingBalanceValid(startingBalance);
		}

	}

	private boolean IsCdAccountStartingBalanceValid(String inputBalance) {
		try {
			double numBalance = Double.parseDouble(inputBalance);
			if (numBalance >= CD_MIN_BALANCE && numBalance <= CD_MAX_BALANCE) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isAccountAPRValid(String inputApr) {
		try {
			double numApr = Double.parseDouble(inputApr);
			if (numApr >= MIN_APR && numApr <= MAX_APR) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}

	}

	private boolean isAccountTypeValid(String accountType) {
		if (accountType.equalsIgnoreCase("checking") || accountType.equalsIgnoreCase("savings")
				|| accountType.equalsIgnoreCase("cd")) {
			return true;
		}
		return false;
	}

}
