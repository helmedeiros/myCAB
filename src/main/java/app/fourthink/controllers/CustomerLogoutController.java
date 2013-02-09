package app.fourthink.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer-logout")
public class CustomerLogoutController {

    @RequestMapping(method = RequestMethod.POST)
    public String submit(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
