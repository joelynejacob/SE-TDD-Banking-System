package banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandStorage {
	private final List<String> invalidCommands = new ArrayList<>();
	private final Map<String, List<String>> validCommands = new HashMap<>();
	protected Bank bank;

	public CommandStorage(Bank bank) {
		this.bank = bank;
	}

	public void addInvalidCommand(String command) {
		invalidCommands.add(command);
	}

	public List<String> returnInvalidCommands() {
		return invalidCommands;
	}

	public void addValidCommand(String inputString) {
		String[] splitString = inputString.split(" ");
		String formattedValidCommand = formatValidCommand(splitString);
		if (splitString[0].equalsIgnoreCase("withdraw") || splitString[0].equalsIgnoreCase("deposit")) {
			insertIntoValidCommandMap(validCommands, splitString[1], formattedValidCommand);
		} else if (splitString[0].equalsIgnoreCase("transfer")) {
			handleTransferCommand(splitString, formattedValidCommand);
		} else if (splitString[0].equalsIgnoreCase("create") && (validCommands.containsKey(splitString[2]))) {
			validCommands.remove(splitString[2]);
		}
	}

	private void handleTransferCommand(String[] splitString, String formattedValidCommand) {
		insertIntoValidCommandMap(validCommands, splitString[1], formattedValidCommand);
		insertIntoValidCommandMap(validCommands, splitString[2], formattedValidCommand);
	}

	public Map<String, List<String>> getValidCommands() {
		return validCommands;
	}

	private void insertIntoValidCommandMap(Map<String, List<String>> validCommands, String accountID, String command) {
		if (validCommands.containsKey(accountID)) {
			validCommands.get(accountID).add(command);
		} else {
			validCommands.put(accountID, new ArrayList<>());
			validCommands.get(accountID).add(command);
		}
	}

	protected String formatValidCommand(String[] validCommand) {
		String firstWord = validCommand[0];
		String formattedFirstWord = firstWord.substring(0, 1).toUpperCase() + firstWord.substring(1).toLowerCase();

		StringBuilder formattedString = new StringBuilder(formattedFirstWord);
		for (int i = 1; i < validCommand.length; i++) {
			formattedString.append(" ").append(validCommand[i]);
		}
		return formattedString.toString();
	}

}
