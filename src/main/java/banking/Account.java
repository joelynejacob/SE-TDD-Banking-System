package banking;

public abstract class Account {
	protected double balance;
	protected String id;
	private double apr;
	private int age = 0;

	protected Account(String ID, double apr) {
		this.apr = apr;
		this.balance = 0;
		this.id = ID;
	}

	public double getBalance() {
		return balance;
	}

	public void depositMoney(double moneyToDeposit) {
		balance = balance + moneyToDeposit;
	}

	public void withdrawMoney(double moneyToWithdraw) {
		balance = balance - moneyToWithdraw;
		if (balance < 0) {
			balance = 0;
		}
	}

	public double getApr() {
		return apr;
	}

	public abstract String getAccountType();

	public abstract boolean isDepositAmountValid(double amount);

	public String getID() {
		return id;
	}

	public abstract boolean isWithdrawalAmountValid(double numDeposit);

	public void calculateAPR() {
		balance += ((apr / 100) / 12) * balance;
	}

	public void increaseAge(int months) {
		this.age += months;
	}

	public int getAge() {
		return age;
	}

	public abstract boolean isWithdrawalTimeValid();

}