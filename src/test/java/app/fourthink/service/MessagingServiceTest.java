package app.fourthink.service;

import app.fourthink.model.Message;
import app.fourthink.model.RecipientKind;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class MessagingServiceTest {

    @Autowired
    private MessagingService service;

    @Test
    public void sendsMessageToCab() {
        Message m = service.send(RecipientKind.CAB, 1L, "Aceite a corrida na rua Sao Pedro");
        assertFalse(m.isRead());
    }

    @Test
    public void rejectsEmptyBody() {
        try {
            service.send(RecipientKind.CAB, 1L, "  ");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void drainReturnsAndMarksRead() {
        service.send(RecipientKind.CUSTOMER, 7L, "Seu carro a caminho");
        service.send(RecipientKind.CUSTOMER, 7L, "Aguarde por favor");
        List<Message> first = service.drain(RecipientKind.CUSTOMER, 7L);
        assertEquals(2, first.size());
        assertTrue(first.get(0).isRead());
        List<Message> second = service.drain(RecipientKind.CUSTOMER, 7L);
        assertEquals(0, second.size());
    }

    @Test
    public void recentIncludesReadMessages() {
        service.send(RecipientKind.CAB, 2L, "Primeira");
        service.send(RecipientKind.CAB, 2L, "Segunda");
        service.drain(RecipientKind.CAB, 2L);
        assertEquals(2, service.recent(RecipientKind.CAB, 2L, 10).size());
    }
}
