package banking;

public class CommandValidator {

	private final int ID_LENGTH = 8;
	private final int STRING_COMPONENTS = 4;
	public Bank bank;

	public CommandValidator(Bank bank) {
		this.bank = bank;
	}

	public boolean isAccountIDValid(String idString) {
		if (idString.matches("\\d+") && idString.length() == ID_LENGTH) {
			return true;
		}
		return false;
	}

	public boolean inputStringHasEnoughComponents(String[] inputString) {
		return (inputString.length >= STRING_COMPONENTS);
	}

	boolean isAmountValid(String[] inputString, boolean isDeposit) {
		try {
			String amount = inputString[2];
			String accountID = inputString[1];
			double numAmount = Double.parseDouble(amount);

			if (isDeposit) {
				return bank.getAccountById(accountID).isDepositAmountValid(numAmount);
			} else {
				return bank.getAccountById(accountID).isWithdrawalAmountValid(numAmount);
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean validate(String inputString) {
		String[] inputSplit = inputString.split("\\s");
		String actionCommand = inputSplit[0];

		if (actionCommand.equalsIgnoreCase("create")) {
			CreateCommandValidator createCommandValidator = new CreateCommandValidator(bank);
			return createCommandValidator.validate(inputSplit);
		} else if (actionCommand.equalsIgnoreCase("deposit")) {
			DepositCommandValidator depositCommandValidator = new DepositCommandValidator(bank);
			return depositCommandValidator.validate(inputSplit);
		} else if (actionCommand.equalsIgnoreCase("withdraw")) {
			WithdrawCommandValidator withdrawCommandValidator = new WithdrawCommandValidator(bank);
			return withdrawCommandValidator.validate(inputSplit);
		} else if (actionCommand.equalsIgnoreCase("transfer")) {
			TransferCommandValidator transferCommandValidator = new TransferCommandValidator(bank);
			return transferCommandValidator.validate(inputSplit);
		} else if (actionCommand.equalsIgnoreCase("pass")) {
			PassTimeCommandValidator passTimeCommandValidator = new PassTimeCommandValidator(bank);
			return passTimeCommandValidator.validate(inputSplit);
		} else {
			return false;
		}
	}
}