package app.fourthink.controllers;

import app.fourthink.model.Cab;
import app.fourthink.model.CabModel;
import app.fourthink.model.CabStatus;
import app.fourthink.persistence.CabModelRepository;
import app.fourthink.service.FleetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cabs")
public class CabController {

    private final FleetService fleet;
    private final CabModelRepository models;

    @Autowired
    public CabController(FleetService fleet, CabModelRepository models) {
        this.fleet = fleet;
        this.models = models;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        List<Cab> cabs = fleet.list();
        model.addAttribute("cabs", cabs);
        return "cabs/list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String form(Model model) {
        List<CabModel> options = models.findAll();
        model.addAttribute("models", options);
        return "cabs/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam("plate") String plate,
                         @RequestParam("modelId") Long modelId) {
        fleet.register(plate, modelId);
        return "redirect:/cabs";
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.POST)
    public String status(@PathVariable Long id,
                         @RequestParam("status") CabStatus status) {
        fleet.updateStatus(id, status);
        return "redirect:/cabs";
    }

    @RequestMapping(value = "/{id}/location", method = RequestMethod.POST)
    public String updateLocation(@PathVariable Long id,
                                  @RequestParam("latitude") double latitude,
                                  @RequestParam("longitude") double longitude) {
        fleet.updateLocation(id, latitude, longitude);
        return "redirect:/cabs";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable Long id) {
        fleet.deregister(id);
        return "redirect:/cabs";
    }
}
