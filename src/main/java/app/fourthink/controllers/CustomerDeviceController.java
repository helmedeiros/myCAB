package app.fourthink.controllers;

import app.fourthink.model.RecipientKind;
import app.fourthink.service.CustomerService;
import app.fourthink.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/customer")
public class CustomerDeviceController {

    private final CustomerService customers;
    private final MessagingService messaging;

    @Autowired
    public CustomerDeviceController(CustomerService customers, MessagingService messaging) {
        this.customers = customers;
        this.messaging = messaging;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String device(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customers.get(id));
        model.addAttribute("recent", messaging.recent(RecipientKind.CUSTOMER, id, 10));
        return "device/customer";
    }
}
