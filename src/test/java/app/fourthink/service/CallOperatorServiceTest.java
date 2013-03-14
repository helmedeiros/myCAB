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
import static org.junit.Assert.assertFalse;
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
    public void requestCallStoresCustomerIdPickupAndDestination() {
        Customer c = signup.signup("Ana", "ana@example.com", "(51) 99999-1111",
                "secret123", null);
        Message message = callOperator.requestCall(c.getId(), "Rua das Flores 12", "Aeroporto");
        assertEquals(c.getId(), message.getSourceCustomerId());
        assertEquals("Rua das Flores 12", message.getPickupAddress());
        assertEquals("Aeroporto", message.getDestinationAddress());
    }

    @Test
    public void messageBodyDoesNotLeakCustomerIdentity() {
        Customer c = signup.signup("Ana", "ana@example.com", "(51) 99999-1111",
                "secret123", null);
        Message message = callOperator.requestCall(c.getId(), "Rua A", "Rua B");
        assertFalse(message.getBody().contains("Ana"));
        assertFalse(message.getBody().contains("99999"));
    }

    @Test
    public void rejectsBlankPickup() {
        Customer c = signup.signup("Ana", "ana@example.com", "(51) 99999-1111",
                "secret123", null);
        try {
            callOperator.requestCall(c.getId(), " ", "Aeroporto");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsBlankDestination() {
        Customer c = signup.signup("Ana", "ana@example.com", "(51) 99999-1111",
                "secret123", null);
        try {
            callOperator.requestCall(c.getId(), "Rua A", null);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsUnknownCustomer() {
        try {
            callOperator.requestCall(9999L, "Rua A", "Rua B");
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
            disabled.requestCall(1L, "Rua A", "Rua B");
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void pendingListReturnsTheRequest() {
        Customer c = signup.signup("Ana", "ana@example.com", "(51) 99999-1111",
                "secret123", null);
        callOperator.requestCall(c.getId(), "Rua A", "Rua B");
        List<Message> pending = messaging.pendingOperatorCalls(0L);
        assertEquals(1, pending.size());
        assertNotNull(pending.get(0).getPickupAddress());
    }
}
