package app.fourthink.service;

import app.fourthink.config.FlowConfig;
import app.fourthink.model.CabCategory;
import app.fourthink.model.Customer;
import app.fourthink.model.Dispatch;
import app.fourthink.model.Location;
import app.fourthink.persistence.CustomerRepository;
import app.fourthink.persistence.DispatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerRequestService {

    private CustomerRepository customers;
    private DispatchRepository dispatches;
    private FlowConfig flows;

    public CustomerRequestService() {
    }

    @Autowired
    public CustomerRequestService(CustomerRepository customers, DispatchRepository dispatches,
                                   FlowConfig flows) {
        this.customers = customers;
        this.dispatches = dispatches;
        this.flows = flows;
    }

    public Dispatch request(Long customerId, double latitude, double longitude,
                             String pickupAddress, String destinationAddress,
                             CabCategory category) {
        if (!flows.isRequestEnabled()) {
            throw new IllegalStateException("self-service request flow is disabled");
        }
        if (pickupAddress == null || pickupAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("pickup address is required");
        }
        if (destinationAddress == null || destinationAddress.trim().isEmpty()) {
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
                pickupAddress.trim(), destinationAddress.trim(), category, true);
        return dispatches.save(dispatch);
    }

    public List<Dispatch> pending() {
        return dispatches.findPendingCustomerRequests();
    }
}
