package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.fail;

public class PlateMoreTest {

    @Test
    public void rejectsShort() {
        try { new Plate("AB-1234"); fail(); } catch (IllegalArgumentException e) {}
    }

    @Test
    public void rejectsLong() {
        try { new Plate("ABCD-1234"); fail(); } catch (IllegalArgumentException e) {}
    }

    @Test
    public void rejectsAllLetters() {
        try { new Plate("ABC-DEFG"); fail(); } catch (IllegalArgumentException e) {}
    }
}
