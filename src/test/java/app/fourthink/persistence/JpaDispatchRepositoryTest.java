package app.fourthink.persistence;

import app.fourthink.model.CabCategory;
import app.fourthink.model.Customer;
import app.fourthink.model.Dispatch;
import app.fourthink.model.Location;
import app.fourthink.model.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class JpaDispatchRepositoryTest {

    @Autowired
    private DispatchRepository dispatches;

    @Autowired
    private CustomerRepository customers;

    private Customer maria;

    @Before
    public void setUp() {
        maria = customers.save(new Customer("Maria", new Phone("(51) 99999-1111"), null));
    }

    @Test
    public void persistsDispatch() {
        Dispatch d = dispatches.save(new Dispatch(maria, new Location(-30.0, -51.0),
                "Rua A", CabCategory.NORMAL));
        assertNotNull(d.getId());
    }

    @Test
    public void listsActive() {
        dispatches.save(new Dispatch(maria, new Location(-30.0, -51.0), "Rua A", CabCategory.NORMAL));
        dispatches.save(new Dispatch(maria, new Location(-30.0, -51.0), "Rua B", CabCategory.CONFORTO));
        assertEquals(2, dispatches.findActive().size());
    }
}
