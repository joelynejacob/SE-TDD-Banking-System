package banking;

public class CommandProcessor {

	public final Bank bank;
	private String commandType;

	public CommandProcessor(Bank bank) {
		this.bank = bank;
	}

	public void execute(String inputString) {
		String[] splitString = inputString.split(" ");
		commandType = splitString[0];
		if (commandType.equalsIgnoreCase("create")) {
			CreateCommandProcessor createCommandProcessor = new CreateCommandProcessor(bank);
			createCommandProcessor.execute(splitString);
		} else if (commandType.equalsIgnoreCase("deposit")) {
			DepositCommandProcessor depositCommandProcessor = new DepositCommandProcessor(bank);
			depositCommandProcessor.execute(splitString);
		} else if (commandType.equalsIgnoreCase("withdraw")) {
			WithdrawCommandProcessor withdrawCommandProcessor = new WithdrawCommandProcessor(bank);
			withdrawCommandProcessor.execute(splitString);
		} else if (commandType.equalsIgnoreCase("transfer")) {
			TransferCommandProcessor transferCommandProcessor = new TransferCommandProcessor(bank);
			transferCommandProcessor.execute(splitString);
		} else if (commandType.equalsIgnoreCase("pass")) {
			PassTimeCommandProcessor passTimeCommandProcessor = new PassTimeCommandProcessor(bank);
			passTimeCommandProcessor.execute(splitString);
		}
	}
}
