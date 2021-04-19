package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.CausaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/causa")
public class CausaController {

	@Autowired
	private CausaService causaService;
	
	@GetMapping("")
	public String causas(final ModelMap model) {

		model.addAttribute("causas", this.causaService.findAll());
		
		return "causas/causaList";
	}
}
