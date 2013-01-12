package app.fourthink.controllers;

import app.fourthink.model.Cab;
import app.fourthink.model.RecipientKind;
import app.fourthink.persistence.CabRepository;
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
@RequestMapping("/fleet")
public class FleetDeviceController {

    private final CabRepository cabs;
    private final FleetService fleet;
    private final MessagingService messaging;

    @Autowired
    public FleetDeviceController(CabRepository cabs, FleetService fleet, MessagingService messaging) {
        this.cabs = cabs;
        this.fleet = fleet;
        this.messaging = messaging;
    }

    @RequestMapping(value = "/{fleetId}", method = RequestMethod.GET)
    public String device(@PathVariable String fleetId, Model model) {
        Cab cab = cabs.findByFleetId(fleetId);
        if (cab == null) {
            throw new IllegalArgumentException("unknown fleet id: " + fleetId);
        }
        model.addAttribute("cab", cab);
        model.addAttribute("recent", messaging.recent(RecipientKind.CAB, cab.getId(), 10));
        return "device/cab";
    }

    @RequestMapping(value = "/{fleetId}/location", method = RequestMethod.POST)
    public String updateLocation(@PathVariable String fleetId,
                                  @RequestParam("latitude") double latitude,
                                  @RequestParam("longitude") double longitude) {
        Cab cab = cabs.findByFleetId(fleetId);
        if (cab == null) {
            throw new IllegalArgumentException("unknown fleet id: " + fleetId);
        }
        fleet.updateLocation(cab.getId(), latitude, longitude);
        return "redirect:/fleet/" + fleetId;
    }
}
