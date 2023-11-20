package banking;

public class CreateCommandProcessor extends CommandProcessor {
	private String accountType;
	private String accountID;
	private String accountAPR;
	private double numAPR;
	private double numBalance;

	public CreateCommandProcessor(Bank bank) {
		super(bank);
	}

	public void execute(String[] splitString) {
		accountType = splitString[1];
		accountID = splitString[2];
		accountAPR = splitString[3];
		numAPR = Double.parseDouble(accountAPR);
		if (accountType.equalsIgnoreCase("checking")) {
			bank.openCheckingAccount(accountID, numAPR);
		} else if (accountType.equalsIgnoreCase("savings")) {
			bank.openSavingsAccount(accountID, numAPR);
		} else if (accountType.equalsIgnoreCase("cd")) {
			numBalance = Double.parseDouble(splitString[4]);
			bank.openCDAccount(accountID, numAPR, numBalance);
		}
	}
}
