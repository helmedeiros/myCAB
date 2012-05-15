package app.fourthink.controllers;

import app.fourthink.model.CabCategory;
import app.fourthink.persistence.CabRepository;
import app.fourthink.service.CustomerService;
import app.fourthink.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/operator")
public class OperatorController {

    private final DispatchService dispatches;
    private final CabRepository cabs;
    private final CustomerService customers;

    @Autowired
    public OperatorController(DispatchService dispatches, CabRepository cabs,
                               CustomerService customers) {
        this.dispatches = dispatches;
        this.cabs = cabs;
        this.customers = customers;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("dispatches", dispatches.active());
        model.addAttribute("fleet", cabs.findAll());
        model.addAttribute("customers", customers.list());
        model.addAttribute("categories", CabCategory.values());
        return "operator/home";
    }
}
