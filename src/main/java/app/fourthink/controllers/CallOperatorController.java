package app.fourthink.controllers;

import app.fourthink.security.SessionGate;
import app.fourthink.service.CallOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/me/call-operator")
public class CallOperatorController {

    private final CallOperatorService callOperator;

    @Autowired
    public CallOperatorController(CallOperatorService callOperator) {
        this.callOperator = callOperator;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String request(HttpSession session) {
        if (!SessionGate.isCustomerAuthenticated(session)) {
            return "redirect:/customer-login";
        }
        callOperator.requestCall(SessionGate.customerId(session));
        return "redirect:/me";
    }
}
