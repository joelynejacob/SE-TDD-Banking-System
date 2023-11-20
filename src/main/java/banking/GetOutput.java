package banking;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GetOutput {

	private Bank bank;
	private CommandStorage commandStorage;

	public GetOutput(Bank bank, CommandStorage commandStorage) {
		this.bank = bank;
		this.commandStorage = commandStorage;
	}

	protected String formatAccountStatus(String accountID) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		decimalFormat.setRoundingMode(RoundingMode.FLOOR);

		Account account = bank.getAccountById(accountID);
		double balance = account.getBalance();
		double apr = account.getApr();
		String formattedBalance = decimalFormat.format(balance);
		String formattedAPR = decimalFormat.format(apr);
		return account.getAccountType() + " " + account.getID() + " " + formattedBalance + " " + formattedAPR;
	}

	public List<String> outputResult() {
		List<String> output = new ArrayList<>();
		for (String accountID : bank.getAccountOrder()) {
			output.add(formatAccountStatus(accountID));
			if (commandStorage.getValidCommands().get(accountID) != null) {
				output.addAll(commandStorage.getValidCommands().get(accountID));
			}
		}
		output.addAll(commandStorage.returnInvalidCommands());
		return output;
	}

}
