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
public class DriverReviewServiceTest {

    @Autowired
    private DriverReviewService review;

    @Autowired
    private DriverSignupService signup;

    @Autowired
    private CabModelRepository models;

    private Long modelId;

    @Before
    public void setUp() {
        modelId = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL)).getId();
    }

    @Test
    public void pendingListSeesNewSignups() {
        signup.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        assertEquals(1, review.pending().size());
    }

    @Test
    public void approveSetsFleetIdAndActivates() {
        Driver d = signup.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        Driver approved = review.approve(d.getId(), "042");
        assertEquals(DriverStatus.ACTIVE, approved.getStatus());
        assertEquals("042", approved.getCab().getFleetId());
    }

    @Test
    public void approveRejectsBlankFleetId() {
        Driver d = signup.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        try {
            review.approve(d.getId(), "  ");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void approveRejectsDuplicateFleetId() {
        Driver a = signup.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        review.approve(a.getId(), "042");
        Driver b = signup.signup("Maria", "maria@example.com", "(51) 99999-1111",
                "DEF654321", "secret123", CabCategory.NORMAL, "DEF-5678", modelId);
        try {
            review.approve(b.getId(), "042");
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void rejectMarksDriverAndClearsCab() {
        Driver d = signup.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        Driver rejected = review.reject(d.getId());
        assertEquals(DriverStatus.REJECTED, rejected.getStatus());
    }

    @Test
    public void editPersonalUpdatesTheDriverFields() {
        Driver d = signup.signup("Jose Wrong", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        review.editPersonal(d.getId(), "Jose Correct", "jose@example.com",
                "(51) 99999-1111", "ABC123456", CabCategory.CONFORTO);
        Driver reloaded = review.get(d.getId());
        assertEquals("Jose Correct", reloaded.getFullName());
        assertEquals(CabCategory.CONFORTO, reloaded.getPreferredCategory());
    }

    @Test
    public void editVehicleUpdatesPlateAndModel() {
        Driver d = signup.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        Long otherModel = models.save(new CabModel("Toyota", "Corolla", CabCategory.CONFORTO)).getId();
        review.editVehicle(d.getId(), "ZZZ-9999", otherModel);
        Driver reloaded = review.get(d.getId());
        assertNotNull(reloaded.getCab());
        assertEquals("ZZZ-9999", reloaded.getCab().getPlate().getValue());
        assertEquals("Toyota", reloaded.getCab().getModel().getMake());
    }
}
