package app.fourthink.service;

import app.fourthink.config.FlowConfig;
import app.fourthink.model.Cab;
import app.fourthink.model.CabCategory;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Customer;
import app.fourthink.model.Dispatch;
import app.fourthink.model.DispatchStatus;
import app.fourthink.model.Driver;
import app.fourthink.model.DriverStatus;
import app.fourthink.model.Location;
import app.fourthink.model.RecipientKind;
import app.fourthink.persistence.CabRepository;
import app.fourthink.persistence.CustomerRepository;
import app.fourthink.persistence.DispatchRepository;
import app.fourthink.persistence.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AutoDispatchService {

    private CustomerRepository customers;
    private CabRepository cabs;
    private DispatchRepository dispatches;
    private DriverRepository drivers;
    private NearestCabFinder finder;
    private MessagingService messaging;
    private FlowConfig flows;

    public AutoDispatchService() {
    }

    @Autowired
    public AutoDispatchService(CustomerRepository customers, CabRepository cabs,
                                DispatchRepository dispatches, DriverRepository drivers,
                                NearestCabFinder finder, MessagingService messaging,
                                FlowConfig flows) {
        this.customers = customers;
        this.cabs = cabs;
        this.dispatches = dispatches;
        this.drivers = drivers;
        this.finder = finder;
        this.messaging = messaging;
        this.flows = flows;
    }

    public Dispatch request(Long customerId, double latitude, double longitude,
                             String pickup, String destination, CabCategory category) {
        if (!flows.isAutoDispatchEnabled()) {
            throw new IllegalStateException("auto-dispatch flow is disabled");
        }
        if (pickup == null || pickup.trim().isEmpty()) {
            throw new IllegalArgumentException("pickup address is required");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("destination address is required");
        }
        if (category == null) {
            throw new IllegalArgumentException("category is required");
        }
        Customer customer = customers.findById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("unknown customer: " + customerId);
        }
        Dispatch dispatch = new Dispatch(customer, new Location(latitude, longitude),
                pickup.trim(), destination.trim(), category, true);
        dispatch = dispatches.save(dispatch);
        proposeNext(dispatch);
        return dispatch;
    }

    public Dispatch accept(Long dispatchId, Long cabId) {
        Dispatch dispatch = require(dispatchId);
        if (dispatch.getStatus() != DispatchStatus.PROPOSED) {
            throw new IllegalStateException("dispatch is not PROPOSED");
        }
        if (dispatch.getProposedCabId() == null || !dispatch.getProposedCabId().equals(cabId)) {
            throw new IllegalStateException("proposal does not belong to cab " + cabId);
        }
        Cab cab = cabs.findById(cabId);
        if (cab == null) {
            throw new IllegalArgumentException("unknown cab: " + cabId);
        }
        dispatch.assignTo(cab);
        cab.setStatus(CabStatus.BUSY);
        cabs.save(cab);
        dispatches.save(dispatch);
        messaging.send(RecipientKind.CUSTOMER, dispatch.getCustomer().getId(),
                buildCustomerMessage(cab, driverFor(cab)));
        return dispatch;
    }

    public Dispatch decline(Long dispatchId, Long cabId) {
        Dispatch dispatch = require(dispatchId);
        if (dispatch.getStatus() != DispatchStatus.PROPOSED) {
            throw new IllegalStateException("dispatch is not PROPOSED");
        }
        if (dispatch.getProposedCabId() == null || !dispatch.getProposedCabId().equals(cabId)) {
            throw new IllegalStateException("proposal does not belong to cab " + cabId);
        }
        dispatch.declineProposed();
        dispatches.save(dispatch);
        if (dispatch.getDeclineCount() >= flows.getAutoDispatchDeclineCap()) {
            messaging.send(RecipientKind.OPERATOR, 0L,
                    "Pedido automatico sem aceite apos " + dispatch.getDeclineCount()
                            + " recusas. Avaliar manualmente.",
                    dispatch.getCustomer().getId(),
                    dispatch.getPickupAddress(), dispatch.getDestinationAddress());
            return dispatch;
        }
        proposeNext(dispatch);
        return dispatch;
    }

    private void proposeNext(Dispatch dispatch) {
        Cab next = finder.findClosestExcluding(dispatch.getPickup(),
                dispatch.getRequestedCategory(), dispatch.getDeclinedCabIds());
        if (next == null) {
            messaging.send(RecipientKind.OPERATOR, 0L,
                    "Pedido automatico sem carros disponiveis. Avaliar manualmente.",
                    dispatch.getCustomer().getId(),
                    dispatch.getPickupAddress(), dispatch.getDestinationAddress());
            return;
        }
        dispatch.proposeTo(next);
        dispatches.save(dispatch);
        messaging.send(RecipientKind.CAB, next.getId(),
                "Nova corrida: " + dispatch.getPickupAddress() + " -> "
                        + dispatch.getDestinationAddress() + ". Abra o painel para aceitar.",
                dispatch.getCustomer().getId());
    }

    private Dispatch require(Long dispatchId) {
        Dispatch d = dispatches.findById(dispatchId);
        if (d == null) {
            throw new IllegalArgumentException("unknown dispatch: " + dispatchId);
        }
        return d;
    }

    private Driver driverFor(Cab cab) {
        for (Driver d : drivers.findByStatus(DriverStatus.ACTIVE)) {
            if (d.getCab() != null && cab.getId().equals(d.getCab().getId())) {
                return d;
            }
        }
        return null;
    }

    private String buildCustomerMessage(Cab cab, Driver driver) {
        StringBuilder sb = new StringBuilder("Seu carro esta a caminho. ");
        if (driver != null) {
            sb.append("Motorista: ").append(driver.getFullName()).append(". ");
        }
        sb.append("Veiculo: ").append(cab.describe()).append(".");
        return sb.toString();
    }
}
