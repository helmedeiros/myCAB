package app.fourthink.controllers;

import app.fourthink.model.Customer;
import app.fourthink.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "q", required = false) String query, Model model) {
        List<Customer> customers = service.search(query);
        model.addAttribute("customers", customers);
        model.addAttribute("query", query == null ? "" : query);
        return "customers/list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String form() {
        return "customers/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam("name") String name,
                         @RequestParam("phone") String phone,
                         @RequestParam(value = "defaultAddress", required = false) String defaultAddress) {
        service.register(name, phone, defaultAddress);
        return "redirect:/customers";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("customer", service.get(id));
        return "customers/edit";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id,
                         @RequestParam("name") String name,
                         @RequestParam("phone") String phone,
                         @RequestParam(value = "defaultAddress", required = false) String defaultAddress) {
        service.update(id, name, phone, defaultAddress);
        return "redirect:/customers";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable Long id) {
        service.deregister(id);
        return "redirect:/customers";
    }
}
