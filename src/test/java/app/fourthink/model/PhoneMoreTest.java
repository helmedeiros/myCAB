package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.fail;

public class PhoneMoreTest {

    @Test
    public void rejectsTooFewDigits() {
        try { new Phone("(11) 1234"); fail(); } catch (IllegalArgumentException e) {}
    }

    @Test
    public void rejectsOnlyParensAndSpaces() {
        try { new Phone("(  ) -    "); fail(); } catch (IllegalArgumentException e) {}
    }
}
