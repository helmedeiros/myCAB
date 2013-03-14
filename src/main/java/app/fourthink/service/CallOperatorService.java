package app.fourthink.service;

import app.fourthink.config.FlowConfig;
import app.fourthink.model.Customer;
import app.fourthink.model.Message;
import app.fourthink.model.RecipientKind;
import app.fourthink.persistence.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CallOperatorService {

    static final Long OPERATOR_INBOX = 0L;

    private CustomerRepository customers;
    private MessagingService messaging;
    private FlowConfig flows;

    public CallOperatorService() {
    }

    @Autowired
    public CallOperatorService(CustomerRepository customers, MessagingService messaging,
                                FlowConfig flows) {
        this.customers = customers;
        this.messaging = messaging;
        this.flows = flows;
    }

    public Message requestCall(Long customerId, String pickup, String destination) {
        if (!flows.isPhoneCallEnabled()) {
            throw new IllegalStateException("call-operator flow is disabled");
        }
        if (pickup == null || pickup.trim().isEmpty()) {
            throw new IllegalArgumentException("pickup address is required");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("destination address is required");
        }
        Customer customer = customers.findById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("unknown customer: " + customerId);
        }
        String body = "Pedido anonimo: " + pickup.trim() + " -> " + destination.trim();
        return messaging.send(RecipientKind.OPERATOR, OPERATOR_INBOX, body,
                customer.getId(), pickup.trim(), destination.trim());
    }
}
