package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Causa;
import org.springframework.samples.petclinic.model.Donacion;
import org.springframework.samples.petclinic.service.CausaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/causa")
public class CausaController {

	@Autowired
	private CausaService causaService;
	
	@GetMapping("")
	public String listCausas(final ModelMap model) {
		List<Double> donacionesT = new ArrayList<Double>();
		for(Causa c : this.causaService.findAll()) {
			donacionesT.add(this.causaService.recaudacionTotal(this.causaService.findDonacionesByCausa(c.getId())));
		}
		model.addAttribute("donacionesTotales", donacionesT);
		model.addAttribute("causas", this.causaService.findAll());
		
		return "causas/causaList";
	}
	
	@GetMapping("/{id}")
	public String causaDetails(@PathVariable("id") int id, ModelMap model) {
		Optional<Causa> causa = causaService.findById(id);
		Double recaudado = causaService.recaudacionTotal(this.causaService.findDonacionesByCausa(id));
		Collection<Donacion> donaciones = causaService.findDonacionesByCausa(id);
		if(causa.isPresent()) {
			model.addAttribute("causa", causa.get());
			model.addAttribute("recaudado", recaudado);
			model.addAttribute("donaciones", donaciones);
			return "causas/causaDetails";
		}else {
			model.addAttribute("message", "No podemos encontrar la causa seleccionada");
			return listCausas(model);
		}
	}
}
