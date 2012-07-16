package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RecipientAddressTest {

    @Test
    public void rejectsNullKind() {
        try { new RecipientAddress(null, 1L); fail(); } catch (IllegalArgumentException e) {}
    }

    @Test
    public void rejectsNullId() {
        try { new RecipientAddress(RecipientKind.CAB, null); fail(); } catch (IllegalArgumentException e) {}
    }

    @Test
    public void equalAddressesShareHashCode() {
        RecipientAddress a = new RecipientAddress(RecipientKind.CAB, 1L);
        RecipientAddress b = new RecipientAddress(RecipientKind.CAB, 1L);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
