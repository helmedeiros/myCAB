package app.fourthink.service;

import app.fourthink.model.RecipientKind;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class MessagingUnreadCountTest {

    @Autowired
    private MessagingService service;

    @Test
    public void countsUnreadOnly() {
        service.send(RecipientKind.CUSTOMER, 5L, "Primeira");
        service.send(RecipientKind.CUSTOMER, 5L, "Segunda");
        service.send(RecipientKind.CUSTOMER, 5L, "Terceira");
        assertEquals(3L, service.unreadCount(RecipientKind.CUSTOMER, 5L));
        service.drain(RecipientKind.CUSTOMER, 5L);
        assertEquals(0L, service.unreadCount(RecipientKind.CUSTOMER, 5L));
    }
}
