package app.fourthink.controllers;

import app.fourthink.model.RecipientKind;
import app.fourthink.service.FleetService;
import app.fourthink.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cab")
public class CabDeviceController {

    private final FleetService fleet;
    private final MessagingService messaging;

    @Autowired
    public CabDeviceController(FleetService fleet, MessagingService messaging) {
        this.fleet = fleet;
        this.messaging = messaging;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String device(@PathVariable Long id, Model model) {
        model.addAttribute("cab", fleet.get(id));
        model.addAttribute("recent", messaging.recent(RecipientKind.CAB, id, 10));
        return "device/cab";
    }

    @RequestMapping(value = "/{id}/location", method = RequestMethod.POST)
    public String updateLocation(@PathVariable Long id,
                                  @RequestParam("latitude") double latitude,
                                  @RequestParam("longitude") double longitude) {
        fleet.updateLocation(id, latitude, longitude);
        return "redirect:/cab/" + id;
    }
}
