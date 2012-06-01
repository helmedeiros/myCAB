package app.fourthink.controllers;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.persistence.CabModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/models")
public class CabModelController {

    private final CabModelRepository models;

    @Autowired
    public CabModelController(CabModelRepository models) {
        this.models = models;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("models", models.findAll());
        model.addAttribute("categories", CabCategory.values());
        return "models/list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam("make") String make,
                         @RequestParam("model") String modelName,
                         @RequestParam("category") CabCategory category) {
        models.save(new CabModel(make, modelName, category));
        return "redirect:/models";
    }
}
