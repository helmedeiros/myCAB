package app.fourthink.security;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PasswordHasherTest {

    private final PasswordHasher hasher = new PasswordHasher();

    @Test
    public void matchesValidPassword() {
        String hash = hasher.hash("secret123");
        assertTrue(hasher.matches("secret123", hash));
    }

    @Test
    public void rejectsWrongPassword() {
        String hash = hasher.hash("secret123");
        assertFalse(hasher.matches("wrong", hash));
    }

    @Test
    public void rejectsShortPassword() {
        try {
            hasher.hash("123");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void differentHashesEachTime() {
        String a = hasher.hash("secret123");
        String b = hasher.hash("secret123");
        assertFalse(a.equals(b));
    }
}
