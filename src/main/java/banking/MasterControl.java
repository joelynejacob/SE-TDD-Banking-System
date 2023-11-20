package banking;

import java.util.List;

public class MasterControl {
	private CommandProcessor commandProcessor;
	private CommandValidator commandValidator;
	private CommandStorage commandStorage;
	private GetOutput getOutput;

	public MasterControl(CommandValidator commandValidator, CommandProcessor commandProcessor,
			CommandStorage commandStorage, GetOutput getOutput) {
		this.commandProcessor = commandProcessor;
		this.commandStorage = commandStorage;
		this.commandValidator = commandValidator;
		this.getOutput = getOutput;
	}

	public List<String> start(List<String> input) {
		for (String command : input) {
			if (commandValidator.validate(command)) {
				commandProcessor.execute(command);
				commandStorage.addValidCommand(command);
			} else {
				commandStorage.addInvalidCommand(command);
			}
		}
		return getOutput.outputResult();
	}

}
