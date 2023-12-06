package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
    Currency SEK, DKK, EUR;

    @Before
    public void setUp() throws Exception {

        SEK = new Currency("SEK", 0.15);
        DKK = new Currency("DKK", 0.20);
        EUR = new Currency("EUR", 1.5);

    }

    @Test
    public void testGetName() {

        assertEquals("SEK", SEK.getName());
        assertEquals("DKK", DKK.getName());
        assertEquals("EUR", EUR.getName());

    }

    @Test
    public void testGetRate() {

        assertEquals(0.15, SEK.getRate(), 0.0);
        assertEquals(0.20, DKK.getRate(), 0.0);
        assertEquals(1.5, EUR.getRate(), 0.0);

    }

    @Test
    public void testSetRate() {

        Currency UAH = new Currency("UAH", 1.2);
        Currency PLN = new Currency("PLN", 1.2);
        Currency USD = new Currency("USD", 1.2);


        UAH.setRate(1.337);
        PLN.setRate(1.234);
        USD.setRate(2.567);


        assertEquals(1.337, UAH.getRate(), 0.0);
        assertEquals(1.234, PLN.getRate(), 0.0);
        assertEquals(2.567, USD.getRate(), 0.0);

    }

    @Test
    public void testGlobalValue() {

        assertEquals(150, SEK.universalValue(1000));
        assertEquals(200, DKK.universalValue(1000));
        assertEquals(1500, EUR.universalValue(1000));

    }

    @Test
    public void testValueInThisCurrency() {

        assertEquals(2667, SEK.valueInThisCurrency(2000, DKK));
        assertEquals(300, EUR.valueInThisCurrency(3000, SEK));

    }

}
