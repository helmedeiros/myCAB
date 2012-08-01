package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomerHasAddressTest {

    @Test
    public void noAddressIsFalse() {
        Customer c = new Customer("Maria", new Phone("(51) 99999-1234"), null);
        assertFalse(c.hasDefaultAddress());
    }

    @Test
    public void blankAddressIsFalse() {
        Customer c = new Customer("Maria", new Phone("(51) 99999-1234"), "   ");
        assertFalse(c.hasDefaultAddress());
    }

    @Test
    public void realAddressIsTrue() {
        Customer c = new Customer("Maria", new Phone("(51) 99999-1234"), "Rua A");
        assertTrue(c.hasDefaultAddress());
    }
}
