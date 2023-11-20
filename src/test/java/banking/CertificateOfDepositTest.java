package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CertificateOfDepositTest {
	CertificateOfDeposit certificateOfDeposit;

	@BeforeEach
	public void setUp() {
		certificateOfDeposit = new CertificateOfDeposit(AccountTest.ID, AccountTest.APR, AccountTest.TEST_BALANCE);
	}

	@Test
	public void starting_balance_of_cd_is_given_starting_balance() {
		double actual = certificateOfDeposit.getBalance();
		assertEquals(AccountTest.TEST_BALANCE, actual);

	}

}
