package app.fourthink.service;

import app.fourthink.model.CabCategory;
import app.fourthink.model.Driver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class DriverSignupServiceTest {

    @Autowired
    private DriverSignupService service;

    @Test
    public void registersANewDriver() {
        Driver d = service.signup("Jose Motorista", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL);
        assertNotNull(d.getId());
        assertEquals("jose@example.com", d.getEmail());
    }

    @Test
    public void rejectsDuplicateEmail() {
        service.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL);
        try {
            service.signup("Outro Jose", "JOSE@example.com", "(51) 99999-1111",
                    "DEF654321", "secret123", CabCategory.NORMAL);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void rejectsInvalidEmail() {
        try {
            service.signup("Jose", "not-an-email", "(51) 99999-0000",
                    "ABC123456", "secret123", CabCategory.NORMAL);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsShortPassword() {
        try {
            service.signup("Jose", "jose@example.com", "(51) 99999-0000",
                    "ABC123456", "1234", CabCategory.NORMAL);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }
}
