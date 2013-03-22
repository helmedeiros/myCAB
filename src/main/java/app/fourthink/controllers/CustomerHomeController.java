package app.fourthink.controllers;

import app.fourthink.config.FlowConfig;
import app.fourthink.model.CabCategory;
import app.fourthink.model.Customer;
import app.fourthink.model.Dispatch;
import app.fourthink.model.DispatchStatus;
import app.fourthink.model.Driver;
import app.fourthink.model.DriverStatus;
import app.fourthink.persistence.CustomerRepository;
import app.fourthink.persistence.DriverRepository;
import app.fourthink.security.SessionGate;
import app.fourthink.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/me")
public class CustomerHomeController {

    private final CustomerRepository customers;
    private final DispatchService dispatches;
    private final DriverRepository drivers;
    private final FlowConfig flows;

    @Autowired
    public CustomerHomeController(CustomerRepository customers, DispatchService dispatches,
                                   DriverRepository drivers, FlowConfig flows) {
        this.customers = customers;
        this.dispatches = dispatches;
        this.drivers = drivers;
        this.flows = flows;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(HttpSession session, Model model) {
        if (!SessionGate.isCustomerAuthenticated(session)) {
            return "redirect:/customer-login";
        }
        Customer customer = customers.findById(SessionGate.customerId(session));
        model.addAttribute("customer", customer);
        model.addAttribute("flows", flows);
        model.addAttribute("categories", CabCategory.values());
        List<Dispatch> history = dispatches.forCustomer(customer.getId());
        model.addAttribute("history", history);
        Dispatch active = null;
        for (Dispatch d : history) {
            if (d.getStatus() == DispatchStatus.REQUESTED ||
                d.getStatus() == DispatchStatus.ASSIGNED) {
                active = d;
                break;
            }
        }
        model.addAttribute("activeDispatch", active);
        if (active != null && active.getAssignedCab() != null) {
            model.addAttribute("assignedDriver", driverFor(active.getAssignedCab().getId()));
        }
        return "customer-home/home";
    }

    private Driver driverFor(Long cabId) {
        for (Driver d : drivers.findByStatus(DriverStatus.ACTIVE)) {
            if (d.getCab() != null && cabId.equals(d.getCab().getId())) {
                return d;
            }
        }
        return null;
    }
}
