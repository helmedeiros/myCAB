package app.fourthink.service;

import app.fourthink.config.FlowConfig;
import app.fourthink.model.CabCategory;
import app.fourthink.model.Customer;
import app.fourthink.model.Dispatch;
import app.fourthink.model.DispatchStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class CustomerRequestServiceTest {

    @Autowired
    private CustomerRequestService request;

    @Autowired
    private CustomerSignupService signup;

    @Test
    public void createsRequestedDispatchMarkedCustomerInitiated() {
        Customer c = signup.signup("Joana", "joana@example.com", "(51) 99999-0001",
                "secret123", null);
        Dispatch d = request.request(c.getId(), -30.0, -51.0,
                "Rua A 10", "Praca B", CabCategory.NORMAL);
        assertEquals(DispatchStatus.REQUESTED, d.getStatus());
        assertTrue(d.isCustomerInitiated());
        assertEquals("Praca B", d.getDestinationAddress());
    }

    @Test
    public void pendingListReturnsTheRequest() {
        Customer c = signup.signup("Joana", "joana@example.com", "(51) 99999-0001",
                "secret123", null);
        request.request(c.getId(), -30.0, -51.0, "Rua A", "Rua B", CabCategory.NORMAL);
        assertEquals(1, request.pending().size());
    }

    @Test
    public void rejectsBlankPickup() {
        Customer c = signup.signup("Joana", "joana@example.com", "(51) 99999-0001",
                "secret123", null);
        try {
            request.request(c.getId(), -30.0, -51.0, " ", "Praca B", CabCategory.NORMAL);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsBlankDestination() {
        Customer c = signup.signup("Joana", "joana@example.com", "(51) 99999-0001",
                "secret123", null);
        try {
            request.request(c.getId(), -30.0, -51.0, "Rua A", null, CabCategory.NORMAL);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsUnknownCustomer() {
        try {
            request.request(9999L, -30.0, -51.0, "Rua A", "Rua B", CabCategory.NORMAL);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsWhenFlowDisabled() {
        FlowConfig off = new FlowConfig(false, false, false, 3);
        CustomerRequestService disabled = new CustomerRequestService(null, null, off);
        try {
            disabled.request(1L, -30.0, -51.0, "Rua A", "Rua B", CabCategory.NORMAL);
            fail();
        } catch (IllegalStateException expected) {
        }
    }
}
