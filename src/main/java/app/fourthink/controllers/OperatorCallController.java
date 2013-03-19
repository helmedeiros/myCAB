package app.fourthink.controllers;

import app.fourthink.config.FlowConfig;
import app.fourthink.model.Customer;
import app.fourthink.model.Message;
import app.fourthink.persistence.CustomerRepository;
import app.fourthink.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/operator/calls")
public class OperatorCallController {

    private final MessagingService messaging;
    private final CustomerRepository customers;
    private final FlowConfig flows;

    @Autowired
    public OperatorCallController(MessagingService messaging, CustomerRepository customers,
                                   FlowConfig flows) {
        this.messaging = messaging;
        this.customers = customers;
        this.flows = flows;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String accept(@PathVariable Long id, Model model) {
        if (!flows.isPhoneCallEnabled()) {
            throw new IllegalStateException("call-operator flow is disabled");
        }
        Message message = messaging.markRead(id);
        Customer customer = null;
        if (message.getSourceCustomerId() != null) {
            customer = customers.findById(message.getSourceCustomerId());
        }
        model.addAttribute("message", message);
        model.addAttribute("customer", customer);
        return "operator-calls/accepted";
    }
}
