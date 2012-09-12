package app.fourthink.service;

import app.fourthink.model.RecipientKind;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class MessagingHasUnreadTest {

    @Autowired
    private MessagingService service;

    @Test
    public void noMessagesReturnsFalse() {
        assertFalse(service.hasUnread(RecipientKind.CAB, 99L));
    }

    @Test
    public void sentButNotDrainedReturnsTrue() {
        service.send(RecipientKind.CAB, 7L, "ola");
        assertTrue(service.hasUnread(RecipientKind.CAB, 7L));
    }

    @Test
    public void drainedReturnsFalse() {
        service.send(RecipientKind.CAB, 7L, "ola");
        service.drain(RecipientKind.CAB, 7L);
        assertFalse(service.hasUnread(RecipientKind.CAB, 7L));
    }
}
