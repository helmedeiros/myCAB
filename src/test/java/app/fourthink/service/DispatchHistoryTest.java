package app.fourthink.service;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Customer;
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
public class DispatchHistoryTest {

    @Autowired
    private DispatchService dispatch;

    @Autowired
    private CustomerService customers;

    @Autowired
    private FleetService fleet;

    @Autowired
    private CabModelRepository models;

    private Customer maria;
    private Long modelId;

    @Before
    public void setUp() {
        modelId = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL)).getId();
        maria = customers.register("Maria", "(51) 99999-1234", "Rua A");
    }

    @Test
    public void listsByCustomer() {
        dispatch.request(maria.getId(), -30.0, -51.0, "Rua A", CabCategory.NORMAL);
        dispatch.request(maria.getId(), -30.0, -51.0, "Rua B", CabCategory.NORMAL);
        assertEquals(2, dispatch.forCustomer(maria.getId()).size());
    }

    @Test
    public void listsByCab() {
        Long cabId = fleet.register("ABC-1111", modelId).getId();
        fleet.updateLocation(cabId, -30.0, -51.0);
        fleet.updateStatus(cabId, CabStatus.FREE);
        Long dispatchId = dispatch.request(maria.getId(), -30.001, -51.001, "Rua A", CabCategory.NORMAL).getId();
        dispatch.assignClosest(dispatchId);
        assertEquals(1, dispatch.forCab(cabId).size());
    }
}
