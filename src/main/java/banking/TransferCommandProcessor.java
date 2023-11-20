package banking;

public class TransferCommandProcessor extends CommandProcessor {
	public TransferCommandProcessor(Bank bank) {
		super(bank);
	}

	public void execute(String[] splitString) {
		String accountID = splitString[1];
		String accountID2 = splitString[2];
		double amount = Double.parseDouble(splitString[3]);
		bank.transferMoneyThroughBank(accountID, accountID2, amount);
	}
}