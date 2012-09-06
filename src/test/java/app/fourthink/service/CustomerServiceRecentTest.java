package app.fourthink.service;

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
public class CustomerServiceRecentTest {

    @Autowired
    private CustomerService customers;

    @Test
    public void recentReturnsLatestFirst() {
        customers.register("Maria", "(51) 99999-1111", null);
        customers.register("Joao", "(51) 99999-2222", null);
        customers.register("Pedro", "(51) 99999-3333", null);
        assertEquals("Pedro", customers.recent(2).get(0).getName());
    }
}
