package app.fourthink.controllers;

import app.fourthink.config.FlowConfig;
import app.fourthink.model.Dispatch;
import app.fourthink.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/operator/requests")
public class OperatorRequestController {

    private final DispatchService dispatches;
    private final FlowConfig flows;

    @Autowired
    public OperatorRequestController(DispatchService dispatches, FlowConfig flows) {
        this.dispatches = dispatches;
        this.flows = flows;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Long id, Model model) {
        if (!flows.isRequestEnabled()) {
            throw new IllegalStateException("request flow is disabled");
        }
        Dispatch dispatch = dispatches.find(id);
        model.addAttribute("dispatch", dispatch);
        return "operator-requests/detail";
    }

    @RequestMapping(value = "/{id}/assign", method = RequestMethod.POST)
    public String assign(@PathVariable Long id) {
        dispatches.assignClosest(id);
        return "redirect:/dispatches/" + id;
    }

    @RequestMapping(value = "/{id}/cancel", method = RequestMethod.POST)
    public String cancel(@PathVariable Long id) {
        dispatches.cancel(id);
        return "redirect:/operator";
    }
}
