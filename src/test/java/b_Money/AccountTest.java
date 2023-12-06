package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {

		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		// all tests failed with NullPointerException here
		SweBank.deposit("Alice", new Money(1000000, SEK));

	}

	@Test
	public void testAddRemoveTimedPayment() {

		assertFalse(testAccount.timedPaymentExists("T0001"));
		testAccount.addTimedPayment("T0001", 1, 2, new Money(10000, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("T0001"));
		testAccount.removeTimedPayment("T0001");
		assertFalse(testAccount.timedPaymentExists("T0001"));

	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {

		assertFalse(testAccount.timedPaymentExists("T0001"));

		testAccount.addTimedPayment("T0001", 1, 2, new Money(10000, SEK), SweBank, "Alice");

		assertEquals(10000000, testAccount.getBalance().getAmount());

		testAccount.tick();
		testAccount.tick();
		assertEquals(9990000, testAccount.getBalance().getAmount());
		assertEquals(1010000, SweBank.getBalance("Alice"));

		testAccount.tick();
		assertEquals(9980000, testAccount.getBalance().getAmount());
		assertEquals(1020000, SweBank.getBalance("Alice"));

	}

	@Test
	public void testAddWithdraw() {

		testAccount.deposit(new Money(10000, SEK));
		assertEquals(10010000, testAccount.getBalance().getAmount());
		testAccount.withdraw(new Money(5000, SEK));
		assertEquals(10005000, testAccount.getBalance().getAmount());

	}
	
	@Test
	public void testGetBalance() {

		assertEquals(10000000, testAccount.getBalance().getAmount());

	}
}
