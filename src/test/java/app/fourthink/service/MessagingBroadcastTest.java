package app.fourthink.service;

import app.fourthink.model.RecipientKind;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class MessagingBroadcastTest {

    @Autowired
    private MessagingService service;

    @Test
    public void sendsCopyToEachRecipient() {
        service.broadcast(RecipientKind.CAB, Arrays.asList(1L, 2L, 3L), "Reuniao da central");
        assertEquals(1L, service.unreadCount(RecipientKind.CAB, 1L));
        assertEquals(1L, service.unreadCount(RecipientKind.CAB, 2L));
        assertEquals(1L, service.unreadCount(RecipientKind.CAB, 3L));
    }
}
