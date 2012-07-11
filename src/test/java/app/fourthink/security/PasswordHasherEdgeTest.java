package app.fourthink.security;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class PasswordHasherEdgeTest {

    private final PasswordHasher hasher = new PasswordHasher();

    @Test
    public void rejectsNullStoredHash() {
        assertFalse(hasher.matches("anything", null));
    }

    @Test
    public void rejectsMalformedStoredHash() {
        assertFalse(hasher.matches("anything", "no-dollar"));
    }
}
