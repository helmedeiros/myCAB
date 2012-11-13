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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class JpaCustomerRepositoryRecentTest {

    @Autowired
    private CustomerRepository customers;

    @Test
    public void recentReturnsNewestFirst() {
        customers.save(new Customer("First", new Phone("(51) 99999-0001"), null));
        customers.save(new Customer("Second", new Phone("(51) 99999-0002"), null));
        customers.save(new Customer("Third", new Phone("(51) 99999-0003"), null));
        assertEquals("Third", customers.findRecent(2).get(0).getName());
    }
}
