package app.fourthink.controllers;

import app.fourthink.model.CabCategory;
import app.fourthink.persistence.CabModelRepository;
import app.fourthink.service.DriverSignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/signup")
public class DriverSignupController {

    private final DriverSignupService service;
    private final CabModelRepository models;

    @Autowired
    public DriverSignupController(DriverSignupService service, CabModelRepository models) {
        this.service = service;
        this.models = models;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(Model model) {
        model.addAttribute("categories", CabCategory.values());
        model.addAttribute("models", models.findAll());
        return "drivers/signup";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@RequestParam("fullName") String fullName,
                         @RequestParam("email") String email,
                         @RequestParam("phone") String phone,
                         @RequestParam("licenseNumber") String licenseNumber,
                         @RequestParam("password") String password,
                         @RequestParam("preferredCategory") CabCategory preferredCategory,
                         @RequestParam("plate") String plate,
                         @RequestParam("modelId") Long modelId,
                         @RequestParam(value = "color", required = false) String color,
                         Model model) {
        try {
            service.signup(fullName, email, phone, licenseNumber, password,
                    preferredCategory, plate, modelId, color);
            return "drivers/signup-success";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("categories", CabCategory.values());
            model.addAttribute("models", models.findAll());
            return "drivers/signup";
        }
    }
}
