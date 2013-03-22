package app.fourthink.controllers;

import app.fourthink.model.CabCategory;
import app.fourthink.security.SessionGate;
import app.fourthink.service.CustomerRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/me/request")
public class CustomerRequestController {

    private final CustomerRequestService request;

    @Autowired
    public CustomerRequestController(CustomerRequestService request) {
        this.request = request;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(HttpSession session,
                          @RequestParam("latitude") double latitude,
                          @RequestParam("longitude") double longitude,
                          @RequestParam("pickup") String pickup,
                          @RequestParam("destination") String destination,
                          @RequestParam("category") CabCategory category) {
        if (!SessionGate.isCustomerAuthenticated(session)) {
            return "redirect:/customer-login";
        }
        request.request(SessionGate.customerId(session), latitude, longitude,
                pickup, destination, category);
        return "redirect:/me";
    }
}
