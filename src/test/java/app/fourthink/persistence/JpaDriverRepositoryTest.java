package app.fourthink.persistence;

import app.fourthink.model.CabCategory;
import app.fourthink.model.Driver;
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
public class JpaDriverRepositoryTest {

    @Autowired
    private DriverRepository drivers;

    @Test
    public void persistsAndRetrievesDriver() {
        Driver saved = drivers.save(new Driver("Jose", "jose@example.com",
                new Phone("(51) 99999-1234"), "ABC123456", "hash$x", CabCategory.NORMAL));
        assertNotNull(saved.getId());
        assertEquals("Jose", drivers.findById(saved.getId()).getFullName());
    }

    @Test
    public void findsByEmail() {
        drivers.save(new Driver("Jose", "jose@example.com", new Phone("(51) 99999-1234"),
                "ABC123456", "hash$x", CabCategory.NORMAL));
        assertNotNull(drivers.findByEmail("jose@example.com"));
        assertNull(drivers.findByEmail("missing@example.com"));
    }

    @Test
    public void findsByLicense() {
        drivers.save(new Driver("Jose", "jose@example.com", new Phone("(51) 99999-1234"),
                "ABC123456", "hash$x", CabCategory.NORMAL));
        assertNotNull(drivers.findByLicenseNumber("ABC123456"));
    }
}
