package app.fourthink.service;

import app.fourthink.model.Cab;
import app.fourthink.model.CabCategory;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Customer;
import app.fourthink.model.Dispatch;
import app.fourthink.model.Location;
import app.fourthink.model.RecipientKind;
import app.fourthink.persistence.CabRepository;
import app.fourthink.persistence.CustomerRepository;
import app.fourthink.persistence.DispatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DispatchService {

    private CustomerRepository customers;
    private CabRepository cabs;
    private DispatchRepository dispatches;
    private NearestCabFinder finder;
    private MessagingService messaging;

    public DispatchService() {
    }

    @Autowired
    public DispatchService(CustomerRepository customers, CabRepository cabs,
                            DispatchRepository dispatches, NearestCabFinder finder,
                            MessagingService messaging) {
        this.customers = customers;
        this.cabs = cabs;
        this.dispatches = dispatches;
        this.finder = finder;
        this.messaging = messaging;
    }

    public Dispatch request(Long customerId, double latitude, double longitude,
                             String pickupAddress, CabCategory category) {
        Customer customer = customers.findById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("unknown customer: " + customerId);
        }
        Dispatch dispatch = new Dispatch(customer, new Location(latitude, longitude),
                pickupAddress, category);
        return dispatches.save(dispatch);
    }

    public Dispatch assignClosest(Long dispatchId) {
        Dispatch dispatch = find(dispatchId);
        Cab cab = finder.findClosest(dispatch.getPickup(), dispatch.getRequestedCategory());
        if (cab == null) {
            throw new IllegalStateException("no available cab for requested category");
        }
        dispatch.assignTo(cab);
        cab.setStatus(CabStatus.BUSY);
        cabs.save(cab);
        dispatches.save(dispatch);
        messaging.send(RecipientKind.CAB, cab.getId(), buildCabMessage(dispatch));
        messaging.send(RecipientKind.CUSTOMER, dispatch.getCustomer().getId(),
                "Seu carro " + cab.getPlate().getValue() + " esta a caminho.");
        return dispatch;
    }

    public Dispatch complete(Long dispatchId) {
        Dispatch dispatch = find(dispatchId);
        Cab cab = dispatch.getAssignedCab();
        dispatch.complete();
        if (cab != null) {
            cab.setStatus(CabStatus.FREE);
            cabs.save(cab);
        }
        return dispatches.save(dispatch);
    }

    public Dispatch cancel(Long dispatchId) {
        Dispatch dispatch = find(dispatchId);
        Cab cab = dispatch.getAssignedCab();
        dispatch.cancel();
        if (cab != null) {
            cab.setStatus(CabStatus.FREE);
            cabs.save(cab);
        }
        return dispatches.save(dispatch);
    }

    public List<Dispatch> active() {
        return dispatches.findActive();
    }

    public List<Dispatch> forCustomer(Long customerId) {
        return dispatches.findByCustomerId(customerId);
    }

    public List<Dispatch> forCab(Long cabId) {
        return dispatches.findByCabId(cabId);
    }

    public Dispatch find(Long id) {
        Dispatch d = dispatches.findById(id);
        if (d == null) {
            throw new IllegalArgumentException("unknown dispatch: " + id);
        }
        return d;
    }

    private String buildCabMessage(Dispatch dispatch) {
        StringBuilder sb = new StringBuilder();
        sb.append("Corrida para ").append(dispatch.getCustomer().getName());
        if (dispatch.getPickupAddress() != null && !dispatch.getPickupAddress().isEmpty()) {
            sb.append(" em ").append(dispatch.getPickupAddress());
        } else {
            sb.append(" nas coordenadas ")
              .append(dispatch.getPickup().getLatitude()).append(",")
              .append(dispatch.getPickup().getLongitude());
        }
        return sb.toString();
    }
}
