package app.fourthink.persistence;

import app.fourthink.model.Message;
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
public class JpaMessageRepositoryTest {

    @Autowired
    private MessageRepository messages;

    @Test
    public void persistsAndDrainsUnreadOnly() {
        Message a = messages.save(new Message(RecipientKind.CAB, 1L, "A"));
        Message b = messages.save(new Message(RecipientKind.CAB, 1L, "B"));
        a.markRead();
        messages.save(a);
        assertEquals(1, messages.findUnread(RecipientKind.CAB, 1L).size());
    }

    @Test
    public void recentReturnsLimit() {
        for (int i = 0; i < 5; i++) {
            messages.save(new Message(RecipientKind.CAB, 1L, "M" + i));
        }
        assertEquals(3, messages.findRecent(RecipientKind.CAB, 1L, 3).size());
    }
}
