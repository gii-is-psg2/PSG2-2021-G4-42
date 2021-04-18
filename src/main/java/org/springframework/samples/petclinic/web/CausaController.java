package org.springframework.samples.petclinic.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Causa;
import org.springframework.samples.petclinic.service.CausaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/causa")
public class CausaController {

	@Autowired
	CausaService causaService;
	
	@GetMapping("")
	public String causas(final ModelMap model) {
		Collection<Causa> si = causaService.findAll();
		model.addAttribute("causas", causaService.findAll());
		
		return "causas/causaList";
	}
}
