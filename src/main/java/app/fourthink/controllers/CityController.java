package app.fourthink.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import app.fourthink.model.City;

@Controller
public class CityController {
	
	@RequestMapping
	public String list(ModelMap model){
		List<City> list = new ArrayList<City>();
		City portoAlegre = new City("Porto Alegre", "Rio Grande do Sul", "RS");
		list.add(portoAlegre);
		
		model.addAttribute("list", list);
		
		return "City/list";
	}


}
