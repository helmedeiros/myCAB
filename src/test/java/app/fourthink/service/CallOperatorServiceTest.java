package app.fourthink.service;

import app.fourthink.config.FlowConfig;
import app.fourthink.model.Customer;
import app.fourthink.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class CallOperatorServiceTest {

    @Autowired
    private CallOperatorService callOperator;

    @Autowired
    private CustomerSignupService signup;

    @Autowired
    private MessagingService messaging;

    @Autowired
    private FlowConfig flows;

    @Test
    public void requestCallSendsMessageWithCustomerId() {
        Customer c = signup.signup("Ana", "ana@example.com", "(51) 99999-1111",
                "secret123", null);
        Message message = callOperator.requestCall(c.getId());
        assertEquals(c.getId(), message.getSourceCustomerId());
        List<Message> pending = messaging.pendingOperatorCalls(0L);
        assertEquals(1, pending.size());
        assertNotNull(pending.get(0).getSourceCustomerId());
    }

    @Test
    public void rejectsUnknownCustomer() {
        try {
            callOperator.requestCall(9999L);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsWhenFlowDisabled() {
        FlowConfig off = new FlowConfig(false, false, false, 3);
        CallOperatorService disabled = new CallOperatorService(
                null, messaging, off);
        try {
            disabled.requestCall(1L);
            fail();
        } catch (IllegalStateException expected) {
        }
    }
}
