package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageTest {

    @Test
    public void newMessageIsUnread() {
        Message m = new Message(RecipientKind.CAB, 1L, "Aceite a corrida");
        assertFalse(m.isRead());
    }

    @Test
    public void markReadFlipsState() {
        Message m = new Message(RecipientKind.CAB, 1L, "Aceite a corrida");
        m.markRead();
        assertTrue(m.isRead());
    }

    @Test
    public void storesRecipientCoordinates() {
        Message m = new Message(RecipientKind.CUSTOMER, 7L, "ola");
        assertEquals(RecipientKind.CUSTOMER, m.getRecipientKind());
        assertEquals(Long.valueOf(7L), m.getRecipientId());
    }
}
