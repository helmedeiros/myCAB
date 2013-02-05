package app.fourthink.controllers;

import app.fourthink.service.CustomerSignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/customer-signup")
public class CustomerSignupController {

    private final CustomerSignupService service;

    @Autowired
    public CustomerSignupController(CustomerSignupService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form() {
        return "customer-auth/signup";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@RequestParam("name") String name,
                          @RequestParam("email") String email,
                          @RequestParam("phone") String phone,
                          @RequestParam("password") String password,
                          @RequestParam(value = "defaultAddress", required = false) String defaultAddress,
                          Model model) {
        try {
            service.signup(name, email, phone, password, defaultAddress);
            return "redirect:/customer-login?signed-up";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "customer-auth/signup";
        }
    }
}
