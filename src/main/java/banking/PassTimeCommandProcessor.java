package banking;

public class PassTimeCommandProcessor extends CommandProcessor {
	public PassTimeCommandProcessor(Bank bank) {
		super(bank);
	}

	public void execute(String[] inputString) {
		int months = Integer.parseInt(inputString[1]);
		bank.passTime(months);
	}

}
