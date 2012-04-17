package app.fourthink.persistence;

import app.fourthink.model.Customer;
import app.fourthink.model.Phone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class JpaCustomerRepositoryTest {

    @Autowired
    private CustomerRepository customers;

    @Test
    public void persistsAndRetrievesCustomer() {
        Customer saved = customers.save(new Customer("Maria Silva", new Phone("(51) 99999-1234"), "Rua das Flores 123"));
        assertNotNull(saved.getId());
        assertEquals("Maria Silva", customers.findById(saved.getId()).getName());
    }

    @Test
    public void searchesByNameFragment() {
        customers.save(new Customer("Maria Silva", new Phone("(51) 99999-1111"), null));
        customers.save(new Customer("Joao Santos", new Phone("(51) 99999-2222"), null));
        assertEquals(1, customers.searchByName("maria").size());
    }

    @Test
    public void findsByPhone() {
        customers.save(new Customer("Maria Silva", new Phone("(51) 99999-1234"), null));
        assertNotNull(customers.findByPhone("(51) 99999-1234"));
    }

    @Test
    public void deletesCustomer() {
        Customer c = customers.save(new Customer("Maria", new Phone("(51) 99999-1234"), null));
        customers.delete(c);
        assertNull(customers.findById(c.getId()));
    }
}
