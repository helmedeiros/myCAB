package app.fourthink.service;

import app.fourthink.config.FlowConfig;
import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Customer;
import app.fourthink.model.Dispatch;
import app.fourthink.model.DispatchStatus;
import app.fourthink.model.RecipientKind;
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class AutoDispatchServiceTest {

    @Autowired
    private FleetService fleet;

    @Autowired
    private CustomerSignupService customerSignup;

    @Autowired
    private CabModelRepository models;

    @Autowired
    private MessagingService messaging;

    @Autowired
    private NearestCabFinder finder;

    @Autowired
    private app.fourthink.persistence.CustomerRepository customers;

    @Autowired
    private app.fourthink.persistence.CabRepository cabs;

    @Autowired
    private app.fourthink.persistence.DispatchRepository dispatches;

    @Autowired
    private app.fourthink.persistence.DriverRepository drivers;

    private AutoDispatchService autoDispatch(FlowConfig flows) {
        return new AutoDispatchService(customers, cabs, dispatches, drivers,
                finder, messaging, flows);
    }

    private Customer customer;
    private Long anyModelId;

    @Before
    public void setUp() {
        anyModelId = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL)).getId();
        customer = customerSignup.signup("Eve", "eve@example.com",
                "(51) 99999-0001", "secret123", null);
    }

    private Long registerFreeCab(String plate, double lat, double lon) {
        Long id = fleet.register(plate, anyModelId).getId();
        fleet.updateLocation(id, lat, lon);
        fleet.updateStatus(id, CabStatus.FREE);
        return id;
    }

    @Test
    public void requestProposesNearestCabAndMessagesIt() {
        Long cabId = registerFreeCab("AAA-0001", -30.001, -51.001);
        AutoDispatchService svc = autoDispatch(new FlowConfig(false, false, true, 3));
        Dispatch d = svc.request(customer.getId(), -30.0, -51.0,
                "Rua A", "Rua B", CabCategory.NORMAL);
        assertEquals(DispatchStatus.PROPOSED, d.getStatus());
        assertEquals(cabId, d.getProposedCabId());
        assertEquals(1, messaging.recent(RecipientKind.CAB, cabId, 5).size());
    }

    @Test
    public void acceptMovesToAssignedAndNotifiesCustomer() {
        Long cabId = registerFreeCab("AAA-0001", -30.001, -51.001);
        AutoDispatchService svc = autoDispatch(new FlowConfig(false, false, true, 3));
        Dispatch d = svc.request(customer.getId(), -30.0, -51.0,
                "Rua A", "Rua B", CabCategory.NORMAL);
        Dispatch accepted = svc.accept(d.getId(), cabId);
        assertEquals(DispatchStatus.ASSIGNED, accepted.getStatus());
        assertEquals(cabId, accepted.getAssignedCab().getId());
        assertEquals(CabStatus.BUSY, fleet.get(cabId).getStatus());
        assertEquals(1, messaging.recent(RecipientKind.CUSTOMER, customer.getId(), 5).size());
    }

    @Test
    public void declineRetriesWithNextClosestCab() {
        Long first = registerFreeCab("AAA-0001", -30.001, -51.001);
        Long second = registerFreeCab("BBB-0002", -30.010, -51.010);
        AutoDispatchService svc = autoDispatch(new FlowConfig(false, false, true, 3));
        Dispatch d = svc.request(customer.getId(), -30.0, -51.0,
                "Rua A", "Rua B", CabCategory.NORMAL);
        assertEquals(first, d.getProposedCabId());
        Dispatch afterDecline = svc.decline(d.getId(), first);
        assertEquals(DispatchStatus.PROPOSED, afterDecline.getStatus());
        assertEquals(second, afterDecline.getProposedCabId());
        assertEquals(1, afterDecline.getDeclineCount());
    }

    @Test
    public void declineCapFallsBackToOperator() {
        Long first = registerFreeCab("AAA-0001", -30.001, -51.001);
        Long second = registerFreeCab("BBB-0002", -30.010, -51.010);
        AutoDispatchService svc = autoDispatch(new FlowConfig(false, false, true, 2));
        Dispatch d = svc.request(customer.getId(), -30.0, -51.0,
                "Rua A", "Rua B", CabCategory.NORMAL);
        svc.decline(d.getId(), first);
        Dispatch afterSecond = svc.decline(d.getId(), second);
        assertEquals(DispatchStatus.REQUESTED, afterSecond.getStatus());
        assertEquals(2, afterSecond.getDeclineCount());
        assertTrue(messaging.pendingOperatorCalls(0L).size() >= 1);
    }

    @Test
    public void noCabsAvailableLandsOnOperator() {
        AutoDispatchService svc = autoDispatch(new FlowConfig(false, false, true, 3));
        Dispatch d = svc.request(customer.getId(), -30.0, -51.0,
                "Rua A", "Rua B", CabCategory.NORMAL);
        assertEquals(DispatchStatus.REQUESTED, d.getStatus());
        assertTrue(messaging.pendingOperatorCalls(0L).size() >= 1);
    }

    @Test
    public void rejectsWhenFlowDisabled() {
        AutoDispatchService svc = autoDispatch(new FlowConfig(false, false, false, 3));
        try {
            svc.request(customer.getId(), -30.0, -51.0, "Rua A", "Rua B", CabCategory.NORMAL);
            fail();
        } catch (IllegalStateException expected) {
        }
    }
}
