package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Causa;
import org.springframework.samples.petclinic.model.Donacion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.CausaService;
import org.springframework.samples.petclinic.service.DonacionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/donacion")
public class DonacionController {
	public static final String MESSAGE = "message";
	public static final String DONACION = "donacion";
	public static final String CAUSA = "causa";
	public static final String VIEWS_DONACIONES_CREATE_OR_UPDATE_DONACION_FORM = "donaciones/CreateOrUpdateDonacionForm";
	
	@Autowired
	DonacionService donacionService;
	
	@Autowired
	CausaService causaService;
	
	@Autowired
	OwnerService ownerService;
	
	
	
	@GetMapping(value="{id}/new")
	public String newDonacion(final ModelMap model, @PathVariable("id") final int id) {
		final Donacion donacion = new Donacion();
		final Optional<Causa> causa = this.causaService.findById(id);
		
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		final Optional<Owner> owner = this.ownerService.findOwnerByUsername(username);
		
		if(causa.isEmpty() || owner.isEmpty()) {
			model.addAttribute(MESSAGE, "Error al hacer la donación");
			model.addAttribute("messageType", "danger");
			return "welcome";
		}
		
		donacion.setCausa(causa.get());
		donacion.setDonante(owner.get());
		donacion.setFechaDonacion(LocalDate.now());
		model.addAttribute(DONACION, donacion);
		model.addAttribute(CAUSA, causa.get());
		
		return VIEWS_DONACIONES_CREATE_OR_UPDATE_DONACION_FORM;
	}
	
	@PostMapping(value="{id}/new")
	public String newDonacionPost( @PathVariable("id") final int id, @Valid final Donacion donacion, final BindingResult result, final ModelMap model) {

		final Optional<Causa> causa = this.causaService.findById(id);
		if(causa.isEmpty()) {
			model.addAttribute(MESSAGE, "Error al hacer la donación");
			model.addAttribute("messageType", "danger");
			return "welcome";
		}
		if(result.hasErrors()) {
			model.addAttribute(DONACION, donacion);
			model.addAttribute(CAUSA, causa.get());
			model.addAttribute("causas", this.causaService.findAll());
			return VIEWS_DONACIONES_CREATE_OR_UPDATE_DONACION_FORM;
		}else {
			try {
				donacion.setId(null);
				this.donacionService.save(donacion);
			} catch (final Exception e) {
				model.clear();
				model.addAttribute(DONACION, donacion);
				model.addAttribute(CAUSA, causa.get());
				model.addAttribute(MESSAGE, result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()));
				return VIEWS_DONACIONES_CREATE_OR_UPDATE_DONACION_FORM;
			}
			return "redirect:/causa/"+id;
			
			
		}
	}
}
