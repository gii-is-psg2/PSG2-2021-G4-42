package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Causa;
import org.springframework.samples.petclinic.model.Donacion;
import org.springframework.samples.petclinic.service.CausaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/causa")
public class CausaController {
	public static final String CAUSA = "causa";
	public static final String VIEWS_CAUSA_CREATE_OR_UPDATE_FORM = "causas/createOrUpdateCausaForm";

	@Autowired
	private CausaService causaService;
	
	@GetMapping("")
	public String listCausas(final ModelMap model) {
		model.addAttribute("causas", this.causaService.findAll());
		
		return "causas/causaList";
	}
	
	@GetMapping("/{id}")
	public String causaDetails(@PathVariable("id") int id, ModelMap model) {
		Optional<Causa> causa = causaService.findById(id);
		Double recaudado = causaService.recaudacionTotal(this.causaService.findDonacionesByCausa(id));
		Collection<Donacion> donaciones = causaService.findDonacionesByCausa(id);
		if(causa.isPresent()) {
			model.addAttribute(CAUSA, causa.get());
			model.addAttribute("recaudado", recaudado);
			model.addAttribute("donaciones", donaciones);
			return "causas/causaDetails";
		}else {
			model.addAttribute("message", "No podemos encontrar la causa seleccionada");
			return listCausas(model);
		}
	}
	
	@GetMapping("/new")
	public String createNewCausa(final ModelMap model) {
		Causa causa = new Causa();
		model.addAttribute(CAUSA, causa);
		return VIEWS_CAUSA_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping("/new")
	public String postNewCausa(@Valid Causa causa, final BindingResult result,final ModelMap model){
		if(result.hasErrors()) {
			model.addAttribute(CAUSA, causa);
			return VIEWS_CAUSA_CREATE_OR_UPDATE_FORM;
		}else {
			try {
				String up = causa.getNombre().toUpperCase();
				causa.setNombre(up);
				this.causaService.save(causa);
		}catch(Exception e) {
			model.addAttribute(CAUSA, causa);
			model.addAttribute("message", result.getAllErrors().stream().map(x->x.getDefaultMessage()).collect(Collectors.toList()));
			return VIEWS_CAUSA_CREATE_OR_UPDATE_FORM;
		}
		return "redirect:/causa/";
		}
	}
	
}

