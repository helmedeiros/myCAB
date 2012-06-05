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
public class FleetStatsTest {

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
    public void emptyFleetIsAllZero() {
        FleetStats stats = fleet.stats();
        assertEquals(0, stats.getTotal());
    }

    @Test
    public void countsByStatus() {
        Long a = fleet.register("ABC-1111", modelId).getId();
        Long b = fleet.register("DEF-2222", modelId).getId();
        fleet.register("GHI-3333", modelId);
        fleet.updateStatus(a, CabStatus.FREE);
        fleet.updateStatus(b, CabStatus.BUSY);
        FleetStats stats = fleet.stats();
        assertEquals(3, stats.getTotal());
        assertEquals(1, stats.getFree());
        assertEquals(1, stats.getBusy());
        assertEquals(1, stats.getOffline());
    }
}
