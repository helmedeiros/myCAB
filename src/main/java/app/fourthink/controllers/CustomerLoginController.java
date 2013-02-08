package app.fourthink.controllers;

import app.fourthink.model.Customer;
import app.fourthink.security.SessionGate;
import app.fourthink.service.CustomerLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer-login")
public class CustomerLoginController {

    private final CustomerLoginService login;

    @Autowired
    public CustomerLoginController(CustomerLoginService login) {
        this.login = login;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(@RequestParam(value = "signed-up", required = false) String signedUp,
                       Model model) {
        if (signedUp != null) {
            model.addAttribute("signedUp", true);
        }
        return "customer-auth/login";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@RequestParam("email") String email,
                         @RequestParam("password") String password,
                         HttpSession session,
                         Model model) {
        Customer customer = login.authenticate(email, password);
        if (customer == null) {
            model.addAttribute("error", "Credenciais invalidas");
            return "customer-auth/login";
        }
        session.setAttribute(SessionGate.CUSTOMER_ID, customer.getId());
        return "redirect:/me";
    }
}
