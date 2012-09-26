package app.fourthink.controllers;

import app.fourthink.persistence.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/driver")
public class DriverHomeController {

    private final DriverRepository drivers;

    @Autowired
    public DriverHomeController(DriverRepository drivers) {
        this.drivers = drivers;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(HttpSession session, Model model) {
        if (!app.fourthink.security.SessionGate.isAuthenticated(session)) {
            return "redirect:/login";
        }
        model.addAttribute("driver", drivers.findById(app.fourthink.security.SessionGate.driverId(session)));
        return "drivers/home";
    }
}
