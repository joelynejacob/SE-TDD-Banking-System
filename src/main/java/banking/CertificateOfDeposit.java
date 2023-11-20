package banking;

public class CertificateOfDeposit extends Account {
	public CertificateOfDeposit(String ID, double apr, double balance) {
		super(ID, apr);
		this.balance = balance;
	}

	@Override
	public boolean isDepositAmountValid(double amount) {
		return false;
	}

	@Override
	public boolean isWithdrawalAmountValid(double amount) {
		if (amount >= super.getBalance()) {
			return true;
		}
		return false;
	}

	@Override
	public String getAccountType() {
		return "Cd";
	}

	@Override
	public void calculateAPR() {
		for (int counter = 0; counter < 4; counter++) {
			super.calculateAPR();
		}
	}

	@Override
	public boolean isWithdrawalTimeValid() {
		if (super.getAge() >= 12) {
			return true;
		}
		return false;
	}
}
