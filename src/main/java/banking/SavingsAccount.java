package banking;

public class SavingsAccount extends Account {
	private boolean withdrawalMadeThisMonth = false;

	public SavingsAccount(String ID, double apr) {
		super(ID, apr);
	}

	@Override
	public boolean isDepositAmountValid(double amount) {
		if (amount >= 0 && amount <= 2500.0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isWithdrawalAmountValid(double amount) {
		if (amount >= 0 && amount <= 1000.0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getAccountType() {
		return "Savings";
	}

	@Override
	public boolean isWithdrawalTimeValid() {
		if (withdrawalMadeThisMonth == true) {
			return false;
		}
		return true;
	}

	public void changeWithdrawalStatus() {
		withdrawalMadeThisMonth = !withdrawalMadeThisMonth;
	}

	@Override
	public void increaseAge(int months) {
		super.increaseAge(months);
		withdrawalMadeThisMonth = false;
	}
}
