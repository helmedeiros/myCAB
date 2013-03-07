package app.fourthink.controllers;

import app.fourthink.model.CabCategory;
import app.fourthink.model.Dispatch;
import app.fourthink.service.CustomerService;
import app.fourthink.service.DispatchService;
import app.fourthink.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/dispatches")
public class DispatchController {

    private final DispatchService dispatches;
    private final CustomerService customers;
    private final MessagingService messaging;

    @Autowired
    public DispatchController(DispatchService dispatches, CustomerService customers,
                               MessagingService messaging) {
        this.dispatches = dispatches;
        this.customers = customers;
        this.messaging = messaging;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        List<Dispatch> active = dispatches.active();
        model.addAttribute("dispatches", active);
        return "dispatches/list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String form(@RequestParam(value = "customerId", required = false) Long customerId,
                       @RequestParam(value = "callId", required = false) Long callId,
                       Model model) {
        model.addAttribute("customers", customers.list());
        model.addAttribute("categories", CabCategory.values());
        if (customerId != null) {
            model.addAttribute("customer", customers.get(customerId));
            model.addAttribute("lockCustomer", true);
        }
        if (callId != null) {
            model.addAttribute("callId", callId);
        }
        return "dispatches/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam("customerId") Long customerId,
                         @RequestParam("latitude") double latitude,
                         @RequestParam("longitude") double longitude,
                         @RequestParam(value = "pickupAddress", required = false) String pickupAddress,
                         @RequestParam("category") CabCategory category,
                         @RequestParam(value = "callId", required = false) Long callId) {
        Dispatch d = dispatches.request(customerId, latitude, longitude, pickupAddress, category);
        if (callId != null) {
            messaging.markRead(callId);
        }
        return "redirect:/dispatches/" + d.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("dispatch", dispatches.find(id));
        return "dispatches/view";
    }

    @RequestMapping(value = "/{id}/assign", method = RequestMethod.POST)
    public String assign(@PathVariable Long id) {
        dispatches.assignClosest(id);
        return "redirect:/dispatches/" + id;
    }

    @RequestMapping(value = "/{id}/complete", method = RequestMethod.POST)
    public String complete(@PathVariable Long id) {
        dispatches.complete(id);
        return "redirect:/dispatches";
    }

    @RequestMapping(value = "/{id}/cancel", method = RequestMethod.POST)
    public String cancel(@PathVariable Long id) {
        dispatches.cancel(id);
        return "redirect:/dispatches";
    }
}
