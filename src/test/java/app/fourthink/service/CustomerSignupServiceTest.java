package app.fourthink.service;

import app.fourthink.model.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class CustomerSignupServiceTest {

    @Autowired
    private CustomerSignupService service;

    @Test
    public void registersACustomerWithLoginCredentials() {
        Customer c = service.signup("Maria Silva", "maria@example.com",
                "(51) 99999-1234", "secret123", "Rua A");
        assertNotNull(c.getId());
        assertEquals("maria@example.com", c.getEmail());
        assertTrue(c.canLogin());
    }

    @Test
    public void rejectsDuplicateEmail() {
        service.signup("Maria", "maria@example.com", "(51) 99999-1234", "secret123", null);
        try {
            service.signup("Outra", "MARIA@example.com", "(51) 99999-5555", "secret456", null);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void rejectsDuplicatePhone() {
        service.signup("Maria", "maria@example.com", "(51) 99999-1234", "secret123", null);
        try {
            service.signup("Outra", "outra@example.com", "(51) 99999-1234", "secret456", null);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void rejectsInvalidEmail() {
        try {
            service.signup("Maria", "not-an-email", "(51) 99999-1234", "secret123", null);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsShortPassword() {
        try {
            service.signup("Maria", "maria@example.com", "(51) 99999-1234", "12", null);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }
}
