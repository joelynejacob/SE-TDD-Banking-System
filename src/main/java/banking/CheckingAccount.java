package banking;

public class CheckingAccount extends Account {
	public CheckingAccount(String ID, double apr) {
		super(ID, apr);
	}

	@Override
	public boolean isDepositAmountValid(double amount) {
		if (amount >= 0 && amount <= 1000.0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isWithdrawalAmountValid(double amount) {
		if (amount >= 0 && amount <= 400.0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getAccountType() {
		return "Checking";
	}

	@Override
	public boolean isWithdrawalTimeValid() {
		return true;
	}
}
