package app.fourthink.controllers;

import app.fourthink.model.Driver;
import app.fourthink.service.DriverLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    static final String SESSION_DRIVER_ID = "driverId";
    static final String SESSION_DRIVER_NAME = "driverName";

    private final DriverLoginService login;

    @Autowired
    public LoginController(DriverLoginService login) {
        this.login = login;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form() {
        return "auth/login";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@RequestParam("email") String email,
                         @RequestParam("password") String password,
                         HttpSession session,
                         Model model) {
        Driver driver = login.authenticate(email, password);
        if (driver == null) {
            model.addAttribute("error", "Credenciais invalidas");
            return "auth/login";
        }
        session.setAttribute(SESSION_DRIVER_ID, driver.getId());
        session.setAttribute(SESSION_DRIVER_NAME, driver.getFullName());
        return "redirect:/driver";
    }
}
