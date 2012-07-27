package app.fourthink.controllers;

import app.fourthink.model.CabCategory;
import app.fourthink.service.DriverDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/drivers")
public class DriverDirectoryController {

    private final DriverDirectory directory;

    @Autowired
    public DriverDirectoryController(DriverDirectory directory) {
        this.directory = directory;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "category", required = false) CabCategory category,
                        Model model) {
        if (category != null) {
            model.addAttribute("drivers", directory.byCategory(category));
        } else {
            model.addAttribute("drivers", directory.all());
        }
        model.addAttribute("selected", category);
        model.addAttribute("categories", CabCategory.values());
        return "drivers/list";
    }
}
