package banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
	private Map<String, Account> accounts;
	private ArrayList<String> accountOrder;

	Bank() {
		accounts = new HashMap<>();
		accountOrder = new ArrayList<>();
	}

	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public void openCheckingAccount(String id, double apr) {
		CheckingAccount account = new CheckingAccount(id, apr);
		accounts.put(id, account);
		accountOrder.add(id);
	}

	public void openSavingsAccount(String id, double apr) {
		SavingsAccount account = new SavingsAccount(id, apr);
		accounts.put(id, account);
		accountOrder.add(id);
	}

	public void openCDAccount(String id, double apr, double balance) {
		CertificateOfDeposit account = new CertificateOfDeposit(id, apr, balance);
		accounts.put(id, account);
		accountOrder.add(id);
	}

	public void addMoneyThroughBank(String accountId, double amount) {
		accounts.get(accountId).depositMoney(amount);
	}

	public void withdrawMoneyThroughBank(String accountId, double amount) {
		Account account = accounts.get(accountId);
		account.withdrawMoney(amount);
		if (account.getAccountType() == "Savings") {
			SavingsAccount savingsAccount = (SavingsAccount) account;
			savingsAccount.changeWithdrawalStatus();

		}
	}

	public boolean accountExistsByQuickID(String accountId) {
		if (accounts.get(accountId) != null) {
			return true;
		} else {
			return false;
		}
	}

	public Account getAccountById(String accountId) {
		return accounts.get(accountId);
	}

	public void transferMoneyThroughBank(String accountID, String accountID2, double amount) {
		if (amount >= getAccountById(accountID).getBalance()) {
			addMoneyThroughBank(accountID2, getAccountById(accountID).getBalance());
			getAccountById(accountID).withdrawMoney(getAccountById(accountID).getBalance());
		} else {
			addMoneyThroughBank(accountID2, amount);
			getAccountById(accountID).withdrawMoney(amount);
		}
	}

	public void passTime(int months) {
		List<String> accountsToRemove = new ArrayList<>();

		for (int counter = 0; counter < months; counter++) {
			updateAccounts(accountsToRemove);
		}

		removeAccounts(accountsToRemove);
	}

	private void updateAccounts(List<String> accountsToRemove) {
		for (String accountId : accounts.keySet()) {
			Account account = accounts.get(accountId);
			if (account.balance == 0) {
				accountsToRemove.add(accountId);
			} else if (account.balance < 100) {
				withdrawMoneyThroughBank(accountId, 25);
			}
			account.calculateAPR();
			account.increaseAge(1);
		}
	}

	private void removeAccounts(List<String> accountsToRemove) {
		for (String accountID : accountsToRemove) {
			accountOrder.remove(accountID);
			accounts.remove(accountID);
		}
	}

	List<String> getAccountOrder() {
		return accountOrder;
	}
}
