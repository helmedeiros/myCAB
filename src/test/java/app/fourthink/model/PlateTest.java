package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PlateTest {

    @Test
    public void normalizesToHyphenatedForm() {
        assertEquals("ABC-1234", new Plate("ABC1234").getValue());
        assertEquals("ABC-1234", new Plate("abc-1234").getValue());
    }

    @Test
    public void rejectsInvalidFormat() {
        try {
            new Plate("12-3456");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsNull() {
        try {
            new Plate(null);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void sameValueIsEqual() {
        assertEquals(new Plate("ABC-1234"), new Plate("ABC1234"));
    }
}
