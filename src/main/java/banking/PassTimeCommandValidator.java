package banking;

public class PassTimeCommandValidator extends CommandValidator {

	private final double PASS_TIME_STRING_LENGTH = 2;

	public PassTimeCommandValidator(Bank bank) {
		super(bank);

	}

	public boolean validate(String[] inputString) {
		if (inputStringHasEnoughComponents(inputString) && IsPassTimeValid(inputString)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean IsPassTimeValid(String[] inputString) {
		try {
			String passTime = inputString[1];
			int numPassTime = Integer.parseInt(passTime);
			if (numPassTime > 0 && numPassTime <= 60) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public boolean inputStringHasEnoughComponents(String[] inputString) {
		if (inputString.length == PASS_TIME_STRING_LENGTH) {
			return true;
		} else {
			return false;
		}
	}
}