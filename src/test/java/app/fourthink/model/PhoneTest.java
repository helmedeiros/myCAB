package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PhoneTest {

    @Test
    public void acceptsBrazilianFormat() {
        assertEquals("(51) 99999-1234", new Phone("(51) 99999-1234").getValue());
    }

    @Test
    public void acceptsInternationalFormat() {
        assertEquals("+55 11 91234-5678", new Phone("+55 11 91234-5678").getValue());
    }

    @Test
    public void rejectsEmpty() {
        try {
            new Phone("");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsLetters() {
        try {
            new Phone("abc");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }
}
