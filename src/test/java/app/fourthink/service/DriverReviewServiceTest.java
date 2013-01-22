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
    public void approveGeneratesFleetIdAndActivates() {
        Driver d = signup.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        Driver approved = review.approve(d.getId());
        assertEquals(DriverStatus.ACTIVE, approved.getStatus());
        assertNotNull(approved.getCab().getFleetId());
        assertEquals(4, approved.getCab().getFleetId().length());
    }

    @Test
    public void approveGivesUniqueFleetIdsToDifferentDrivers() {
        Driver a = signup.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        Driver approvedA = review.approve(a.getId());
        Driver b = signup.signup("Maria", "maria@example.com", "(51) 99999-1111",
                "DEF654321", "secret123", CabCategory.NORMAL, "DEF-5678", modelId);
        Driver approvedB = review.approve(b.getId());
        assertNotNull(approvedA.getCab().getFleetId());
        assertNotNull(approvedB.getCab().getFleetId());
        org.junit.Assert.assertFalse(
                approvedA.getCab().getFleetId().equals(approvedB.getCab().getFleetId()));
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
    public void editVehicleUpdatesPlateModelAndColor() {
        Driver d = signup.signup("Jose", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL, "ABC-1234", modelId);
        Long otherModel = models.save(new CabModel("Toyota", "Corolla", CabCategory.CONFORTO)).getId();
        review.editVehicle(d.getId(), "ZZZ-9999", otherModel, "branco");
        Driver reloaded = review.get(d.getId());
        assertNotNull(reloaded.getCab());
        assertEquals("ZZZ-9999", reloaded.getCab().getPlate().getValue());
        assertEquals("Toyota", reloaded.getCab().getModel().getMake());
        assertEquals("branco", reloaded.getCab().getColor());
    }
}
