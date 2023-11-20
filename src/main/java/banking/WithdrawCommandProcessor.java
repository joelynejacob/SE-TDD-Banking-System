package banking;

public class WithdrawCommandProcessor extends CommandProcessor {

	public WithdrawCommandProcessor(Bank bank) {
		super(bank);
	}

	public void execute(String[] splitString) {
		String accountID = splitString[1];
		double amount = Double.parseDouble(splitString[2]);
		bank.withdrawMoneyThroughBank(accountID, amount);
	}

}
