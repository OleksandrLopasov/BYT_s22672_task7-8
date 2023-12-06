package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
    Currency SEK, DKK;
    Bank SweBank, Nordea, DanskeBank;

    @Before
    public void setUp() throws Exception {

        DKK = new Currency("DKK", 0.20);
        SEK = new Currency("SEK", 0.15);
        SweBank = new Bank("SweBank", SEK);
        Nordea = new Bank("Nordea", SEK);
        DanskeBank = new Bank("DanskeBank", DKK);
        SweBank.openAccount("Ulrika");
        SweBank.openAccount("Bob");
        Nordea.openAccount("Bob");
        DanskeBank.openAccount("Gertrud");

    }

    @Test
    public void testGetName() {

        assertEquals("SweBank", SweBank.getName());
        assertEquals("Nordea", Nordea.getName());
        assertEquals("DanskeBank", DanskeBank.getName());

    }

    @Test
    public void testGetCurrency() {

        assertEquals(SEK, SweBank.getCurrency());
        assertEquals(SEK, Nordea.getCurrency());
        assertEquals(DKK, DanskeBank.getCurrency());

    }

    @Test
    public void testOpenAccount() throws AccountExistsException {

        SweBank.openAccount("Gavin");
        Nordea.openAccount("Alice");

    }

    @Test(expected = AccountExistsException.class)
    public void testOpenAccountFailure() throws AccountExistsException {

        // failed - exception not thrown
        SweBank.openAccount("Ulrika");

    }

    @Test
    public void testDeposit() throws AccountDoesNotExistException {

        // failed
        SweBank.deposit("Ulrika", new Money(10000, SEK));
        assertEquals(10000, SweBank.getBalance("Ulrika"));

        SweBank.deposit("Bob", new Money(10000, DKK));
        assertEquals(13333, SweBank.getBalance("Bob"));

    }

    @Test(expected = AccountDoesNotExistException.class)
    public void testDepositFailure() throws AccountDoesNotExistException {

        // failed
        Nordea.deposit("Gavin", new Money(10000, SEK));

    }

    @Test
    public void testWithdraw() throws AccountDoesNotExistException {

        // failed - AccountDoesNotExistException
        SweBank.withdraw("Ulrika", new Money(10000, SEK));
        assertEquals(-10000, SweBank.getBalance("Ulrika"));

        SweBank.withdraw("Bob", new Money(10000, DKK));
        assertEquals(-13333, SweBank.getBalance("Bob"));

    }

    @Test(expected = AccountDoesNotExistException.class)
    public void testWithdrawFailure() throws AccountDoesNotExistException {

        Nordea.withdraw("Gavin", new Money(10000, SEK));

    }

    @Test
    public void testGetBalance() throws AccountDoesNotExistException {

        // failed
        SweBank.deposit("Ulrika", new Money(10000, SEK));
        assertEquals(10000, SweBank.getBalance("Ulrika"));

    }

    @Test(expected = AccountDoesNotExistException.class)
    public void testGetBalanceFailure() throws AccountDoesNotExistException {

        assertEquals(10000, Nordea.getBalance("Gavin"));

    }

    @Test
    public void testTransferSameBank() throws AccountDoesNotExistException {

        // failed
        SweBank.deposit("Bob", new Money(10000, SEK));
        SweBank.transfer("Bob", "Ulrika", new Money(4000, SEK));

        assertEquals(4000, SweBank.getBalance("Ulrika"));
        assertEquals(6000, SweBank.getBalance("Bob"));

    }

    @Test
    public void testTransferDifferentBanks() throws AccountDoesNotExistException {

        // failed
        DanskeBank.deposit("Gertrud", new Money(10000, DKK));
        DanskeBank.transfer("Gertrud", SweBank, "Ulrika", new Money(4000, DKK));

        assertEquals(5333, SweBank.getBalance("Ulrika"));
        assertEquals(6000, DanskeBank.getBalance("Gertrud"));

    }

    @Test
    public void testTimedPayment() throws AccountDoesNotExistException {

        // failed
        SweBank.deposit("Ulrika", new Money(100000, SEK));

        SweBank.addTimedPayment(
                "Ulrika",
                "T0002",
                1, 2,
                new Money(10000, SEK),
                DanskeBank, "Gertrud");

        assertEquals(100000, SweBank.getBalance("Ulrika"));

        SweBank.tick();
        SweBank.tick();
        assertEquals(90000, SweBank.getBalance("Ulrika"));
        assertEquals(7500, DanskeBank.getBalance("Gertrud"));

        SweBank.tick();
        assertEquals(80000, SweBank.getBalance("Ulrika"));
        assertEquals(15000, DanskeBank.getBalance("Gertrud"));

    }
}
