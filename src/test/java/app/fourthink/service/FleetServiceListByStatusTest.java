package app.fourthink.service;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.CabStatus;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class FleetServiceListByStatusTest {

    @Autowired
    private FleetService fleet;

    @Autowired
    private CabModelRepository models;

    private Long modelId;

    @Before
    public void setUp() {
        modelId = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL)).getId();
    }

    @Test
    public void filtersByOfflineStatus() {
        fleet.register("ABC-1111", modelId);
        fleet.register("DEF-2222", modelId);
        assertEquals(2, fleet.listByStatus(CabStatus.OFFLINE).size());
        assertEquals(0, fleet.listByStatus(CabStatus.FREE).size());
    }

    @Test
    public void countsReadyCabs() {
        Long id = fleet.register("ABC-1111", modelId).getId();
        fleet.updateLocation(id, -30.0, -51.0);
        fleet.updateStatus(id, CabStatus.FREE);
        assertEquals(1L, fleet.readyCabCount());
    }
}
