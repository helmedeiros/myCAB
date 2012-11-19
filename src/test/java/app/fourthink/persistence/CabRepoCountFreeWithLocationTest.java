package app.fourthink.persistence;

import app.fourthink.model.Cab;
import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Location;
import app.fourthink.model.Plate;
import org.junit.Before;
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
public class CabRepoCountFreeWithLocationTest {

    @Autowired
    private CabRepository cabs;

    @Autowired
    private CabModelRepository models;

    private CabModel model;

    @Before
    public void setUp() {
        model = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL));
    }

    @Test
    public void countsOnlyFreeCabsWithLocation() {
        Cab withLoc = new Cab(new Plate("ABC-1111"), model);
        withLoc.setStatus(CabStatus.FREE);
        withLoc.updateLocation(new Location(-30.0, -51.0));
        cabs.save(withLoc);

        Cab withoutLoc = new Cab(new Plate("DEF-2222"), model);
        withoutLoc.setStatus(CabStatus.FREE);
        cabs.save(withoutLoc);

        assertEquals(1L, cabs.countFreeWithLocation());
    }
}
