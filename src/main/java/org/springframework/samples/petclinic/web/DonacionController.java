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
	
	
	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("donante", "fechaDonante");
	}
	
	@GetMapping(value="/new")
	public String newDonacion(final ModelMap model) {
		model.addAttribute("donacion", new Donacion());
		model.addAttribute("causas", this.causaService.findAll());
		return "donaciones/CreateOrUpdateDonacionForm";
	}
	
	@PostMapping(value="/new")
	public String newDonacionPost(Donacion donacion, final BindingResult result, final ModelMap model, Principal principal) {
		if(result.hasErrors()) {
			model.clear();
			model.addAttribute("donacion", donacion);
			model.addAttribute("message", result.getFieldErrors().stream().map(x->x.getField()).collect(Collectors.toList()));
			model.addAttribute("causas", this.causaService.findAll());
			return "donaciones/CreateOrUpdateDonacionForm";
		}else {
			try {
				donacion.setFechaDonacion(LocalDate.now());
				donacion.setDonante(this.ownerService.findOwnerByUsername(principal.getName()).get());
				this.donacionService.save(donacion);
			} catch (Exception e) {
				model.clear();
				model.addAttribute("donacion", donacion);
				model.addAttribute("causa", this.causaService.findAll());
				model.addAttribute("message", result.getAllErrors().stream().map(x->x.getDefaultMessage()).collect(Collectors.toList()));
				return "donaciones/CreateOrUpdateDonacionForm";
			}
			return "redirect:/causa/";
			
			
		}
	}
}
