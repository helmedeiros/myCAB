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
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    @Test
    public void registersACustomer() {
        Customer c = service.register("Maria Silva", "(51) 99999-1234", "Rua A");
        assertEquals("Maria Silva", c.getName());
    }

    @Test
    public void rejectsDuplicatePhone() {
        service.register("Maria Silva", "(51) 99999-1234", null);
        try {
            service.register("Outra Maria", "(51) 99999-1234", null);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void updatesCustomer() {
        Customer c = service.register("Maria Silva", "(51) 99999-1234", null);
        Customer updated = service.update(c.getId(), "Maria Andrade", "(51) 88888-9999", "Rua B");
        assertEquals("Maria Andrade", updated.getName());
        assertEquals("(51) 88888-9999", updated.getPhone().getValue());
    }

    @Test
    public void searchFiltersByFragment() {
        service.register("Maria Silva", "(51) 99999-1111", null);
        service.register("Joao Santos", "(51) 99999-2222", null);
        assertEquals(1, service.search("santos").size());
    }

    @Test
    public void blankSearchReturnsAll() {
        service.register("Maria Silva", "(51) 99999-1111", null);
        service.register("Joao Santos", "(51) 99999-2222", null);
        assertEquals(2, service.search("").size());
    }
}
