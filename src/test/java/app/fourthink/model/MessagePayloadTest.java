package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessagePayloadTest {

    @Test
    public void carriesIdBodyAndTimestamp() {
        Message m = new Message(RecipientKind.CAB, 1L, "ola");
        MessagePayload p = MessagePayload.of(m);
        assertEquals("ola", p.getBody());
        assertTrue(p.getCreatedAt() > 0);
    }
}
