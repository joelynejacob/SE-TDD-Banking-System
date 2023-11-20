package banking;

public class DepositCommandProcessor extends CommandProcessor {
	public DepositCommandProcessor(Bank bank) {
		super(bank);
	}

	public void execute(String[] splitString) {
		String accountID = splitString[1];
		double amount = Double.parseDouble(splitString[2]);
		bank.addMoneyThroughBank(accountID, amount);
	}
}
