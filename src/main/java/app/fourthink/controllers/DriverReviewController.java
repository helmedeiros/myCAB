package app.fourthink.controllers;

import app.fourthink.model.CabCategory;
import app.fourthink.persistence.CabModelRepository;
import app.fourthink.service.DriverReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/review")
public class DriverReviewController {

    private final DriverReviewService review;
    private final CabModelRepository models;

    @Autowired
    public DriverReviewController(DriverReviewService review, CabModelRepository models) {
        this.review = review;
        this.models = models;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String queue(Model model) {
        model.addAttribute("pending", review.pending());
        return "review/queue";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("driver", review.get(id));
        model.addAttribute("categories", CabCategory.values());
        model.addAttribute("models", models.findAll());
        return "review/detail";
    }

    @RequestMapping(value = "/{id}/personal", method = RequestMethod.POST)
    public String savePersonal(@PathVariable Long id,
                                @RequestParam("fullName") String fullName,
                                @RequestParam("email") String email,
                                @RequestParam("phone") String phone,
                                @RequestParam("licenseNumber") String licenseNumber,
                                @RequestParam("preferredCategory") CabCategory preferredCategory) {
        review.editPersonal(id, fullName, email, phone, licenseNumber, preferredCategory);
        return "redirect:/review/" + id;
    }

    @RequestMapping(value = "/{id}/vehicle", method = RequestMethod.POST)
    public String saveVehicle(@PathVariable Long id,
                               @RequestParam("plate") String plate,
                               @RequestParam("modelId") Long modelId) {
        review.editVehicle(id, plate, modelId);
        return "redirect:/review/" + id;
    }

    @RequestMapping(value = "/{id}/approve", method = RequestMethod.POST)
    public String approve(@PathVariable Long id, @RequestParam("fleetId") String fleetId) {
        review.approve(id, fleetId);
        return "redirect:/review";
    }

    @RequestMapping(value = "/{id}/reject", method = RequestMethod.POST)
    public String reject(@PathVariable Long id) {
        review.reject(id);
        return "redirect:/review";
    }
}
