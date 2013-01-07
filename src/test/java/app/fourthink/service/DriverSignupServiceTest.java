package app.fourthink.service;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.Driver;
import app.fourthink.model.DriverStatus;
import app.fourthink.persistence.CabModelRepository;
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
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class DriverSignupServiceTest {

    @Autowired
    private DriverSignupService service;

    @Autowired
    private CabModelRepository models;

    private Long modelId;

    @Before
    public void setUp() {
        modelId = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL)).getId();
    }

    @Test
    public void registersANewDriverAsPendingWithCab() {
        Driver d = service.signup("Jose Motorista", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        assertNotNull(d.getId());
        assertEquals("jose@example.com", d.getEmail());
        assertEquals(DriverStatus.PENDING, d.getStatus());
        assertNotNull(d.getCab());
        assertEquals("ABC-1234", d.getCab().getPlate().getValue());
    }

    @Test
    public void rejectsDuplicateEmail() {
        service.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        try {
            service.signup("Outro Jose", "JOSE@example.com", "(51) 99999-1111",
                    "DEF654321", "secret123", CabCategory.NORMAL, "DEF-5678", modelId);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void rejectsDuplicatePlate() {
        service.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        try {
            service.signup("Outro", "outro@example.com", "(51) 99999-1111",
                    "DEF654321", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void rejectsInvalidEmail() {
        try {
            service.signup("Jose", "not-an-email", "(51) 99999-0000",
                    "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsShortPassword() {
        try {
            service.signup("Jose", "jose@example.com", "(51) 99999-0000",
                    "ABC123456", "1234", CabCategory.NORMAL, "ABC-1234", modelId);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void rejectsMissingModel() {
        try {
            service.signup("Jose", "jose@example.com", "(51) 99999-0000",
                    "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", 99999L);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }
}
