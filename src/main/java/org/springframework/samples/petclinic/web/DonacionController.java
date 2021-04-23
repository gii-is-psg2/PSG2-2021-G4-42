package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Donacion;
import org.springframework.samples.petclinic.service.CausaService;
import org.springframework.samples.petclinic.service.DonacionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/donacion")
public class DonacionController {
	
	@Autowired
	DonacionService donacionService;
	
	@Autowired
	CausaService causaService;
	
	@Autowired
	OwnerService ownerService;
	
	
	
	@GetMapping(value="{id}/new")
	public String newDonacion(final ModelMap model, @PathVariable("id") int id, Principal principal) {
		Donacion donacion = new Donacion();
		donacion.setCausa(this.causaService.findById(id).get());
		donacion.setFechaDonacion(LocalDate.now());
		donacion.setDonante(this.ownerService.findOwnerByUsername(principal.getName()).get());
		model.addAttribute("donacion", donacion);
		model.addAttribute("causa", this.causaService.findById(id).get());
		
		return "donaciones/CreateOrUpdateDonacionForm";
	}
	
	@PostMapping(value="{id}/new")
	public String newDonacionPost( @PathVariable("id") int id, @Valid Donacion donacion, final BindingResult result, final ModelMap model) {
		System.out.println(donacion);
		System.out.println("Hola");
		if(result.hasErrors()) {
			model.addAttribute("donacion", donacion);
			model.addAttribute("causa", this.causaService.findById(id).get());
			model.addAttribute("causas", this.causaService.findAll());
			return "donaciones/CreateOrUpdateDonacionForm";
		}else {
			try {
				donacion.setId(null);
				this.donacionService.save(donacion);
			} catch (Exception e) {
				model.clear();
				model.addAttribute("donacion", donacion);
				model.addAttribute("causa", this.causaService.findById(id).get());
				model.addAttribute("message", result.getAllErrors().stream().map(x->x.getDefaultMessage()).collect(Collectors.toList()));
				return "donaciones/CreateOrUpdateDonacionForm";
			}
			return "redirect:/causa/";
			
			
		}
	}
}
