package app.fourthink.controllers;

import app.fourthink.config.FlowConfig;
import app.fourthink.model.CabCategory;
import app.fourthink.model.Dispatch;
import app.fourthink.model.Message;
import app.fourthink.persistence.CabRepository;
import app.fourthink.service.CustomerRequestService;
import app.fourthink.service.CustomerService;
import app.fourthink.service.DispatchService;
import app.fourthink.service.DriverReviewService;
import app.fourthink.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/operator")
public class OperatorController {

    static final Long OPERATOR_INBOX = 0L;

    private final DispatchService dispatches;
    private final CabRepository cabs;
    private final CustomerService customers;
    private final DriverReviewService review;
    private final MessagingService messaging;
    private final FlowConfig flows;
    private final CustomerRequestService customerRequests;

    @Autowired
    public OperatorController(DispatchService dispatches, CabRepository cabs,
                               CustomerService customers, DriverReviewService review,
                               MessagingService messaging,
                               FlowConfig flows,
                               CustomerRequestService customerRequests) {
        this.dispatches = dispatches;
        this.cabs = cabs;
        this.customers = customers;
        this.review = review;
        this.messaging = messaging;
        this.flows = flows;
        this.customerRequests = customerRequests;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("dispatches", visibleDispatches());
        model.addAttribute("fleet", cabs.findAll());
        model.addAttribute("customers", customers.list());
        model.addAttribute("categories", CabCategory.values());
        List<?> pending = review.pending();
        model.addAttribute("pendingDrivers", pending);
        model.addAttribute("pendingCount", pending.size());
        model.addAttribute("flows", flows);
        model.addAttribute("operatorCalls", incomingCalls());
        model.addAttribute("customerRequests", pendingCustomerRequests());
        return "operator/home";
    }

    private List<Dispatch> visibleDispatches() {
        List<Dispatch> visible = new ArrayList<Dispatch>();
        for (Dispatch d : dispatches.active()) {
            if (d.isCustomerInitiated()
                    && d.getStatus() == app.fourthink.model.DispatchStatus.REQUESTED) {
                continue;
            }
            visible.add(d);
        }
        return visible;
    }

    private List<Map<String, Object>> pendingCustomerRequests() {
        List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
        if (!flows.isRequestEnabled()) {
            return out;
        }
        for (Dispatch d : customerRequests.pending()) {
            Map<String, Object> entry = new LinkedHashMap<String, Object>();
            entry.put("id", d.getId());
            entry.put("pickup", d.getPickupAddress());
            entry.put("destination", d.getDestinationAddress());
            entry.put("category", d.getRequestedCategory());
            entry.put("createdAt", d.getCreatedAt());
            out.add(entry);
        }
        return out;
    }

    private List<Map<String, Object>> incomingCalls() {
        List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
        if (!flows.isPhoneCallEnabled()) {
            return out;
        }
        for (Message m : messaging.pendingOperatorCalls(OPERATOR_INBOX)) {
            Map<String, Object> entry = new LinkedHashMap<String, Object>();
            entry.put("id", m.getId());
            entry.put("createdAt", m.getCreatedAt());
            entry.put("pickup", m.getPickupAddress());
            entry.put("destination", m.getDestinationAddress());
            out.add(entry);
        }
        return out;
    }
}
