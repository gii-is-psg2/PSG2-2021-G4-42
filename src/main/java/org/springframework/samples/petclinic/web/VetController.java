/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.SpecialtyService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.exceptions.VetNoEncontradoException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	public static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";
	public static final String MESSAGE = "message";
	
	private final VetService vetService;
	private final SpecialtyService specialtyService;

	@ModelAttribute("especialidades")
	public Map<Integer, String> listaEspecialidades() {
		return StreamSupport.stream(this.specialtyService.findAll().spliterator(), false).collect(Collectors.toMap(BaseEntity::getId, NamedEntity::getName));
	}

	@Autowired
	public VetController(final VetService clinicService, final SpecialtyService specialtyService) {
		this.vetService = clinicService;
		this.specialtyService = specialtyService;
	}

	@GetMapping(value = { "/vets" })
	public String showVetList(final Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of
		// Vet
		// objects
		// so it is simpler for Object-Xml mapping
		final Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@GetMapping(value = { "/vets.xml" })
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of
		// Vet
		// objects
		// so it is simpler for JSon/Object mapping
		final Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}
	
	@GetMapping(value="/vets/{vetId}/delete")
	public String deleteVet(@PathVariable("vetId") final int vetId, final Map<String, Object> model) throws VetNoEncontradoException {
		final Optional<? extends GrantedAuthority> rolOptional = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst();
		String rol = "";
		if(rolOptional.isPresent()) {
			rol = rolOptional.get().toString();
		}
		if(rol.equals("admin")) {
			try {
				final Optional<Vet> vet = this.vetService.findById(vetId);
				if(vet.isEmpty()) {
					model.put(MESSAGE, "Problema a eliminar un veterinario");
					model.put("messageType", "danger");
					return "welcome";
				}
				this.vetService.delete(vet.get());
			}catch (final Exception e) {
				throw new VetNoEncontradoException();
			}
		}
		return this.showVetList(model);
	}
	
	

	/*
	 * CREAR
	 */
	@GetMapping(value = "/vets/new")
	public String initCreationForm(final Map<String, Object> model) {
		final Vet vet = new Vet();
		model.put("vet", vet);
		return VetController.VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/vets/new")
	public String processCreationForm(@Valid final Vet vet, final BindingResult result, final ModelMap model, @RequestParam(required=false) final List<Specialty> specialties) {
		if (result.hasErrors()) {
			return VetController.VIEWS_VET_CREATE_OR_UPDATE_FORM;
		} else {
			vet.setSpecialties(specialties);
			this.vetService.save(vet);
			model.addAttribute(MESSAGE, "Veterinario añadido correctamente");
			return this.showVetList(model);
		}
	}

	/*
	 * EDITAR
	 */
	@GetMapping(value = "/vets/{id}/edit")
	public String initUpdateVetForm(@PathVariable("id") final int id, final Model model) {
		final Optional<Vet> vet = this.vetService.findById(id);
		if(vet.isEmpty()) {
			model.addAttribute(MESSAGE, "Problema a eliminar un veterinario");
			model.addAttribute("messageType", "danger");
			return "welcome";
		}
		
		model.addAttribute(vet.get());
		return VetController.VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/vets/{id}/edit")
	public String processUpdateVetForm(@Valid final Vet vet, final BindingResult result, @PathVariable("id") final int id, final ModelMap model, @RequestParam(required=false) final List<Specialty> specialties) {
		if (result.hasErrors()) {
			return VetController.VIEWS_VET_CREATE_OR_UPDATE_FORM;
		} else {
			vet.setId(id);
			vet.setSpecialties(specialties);
			this.vetService.save(vet);
			model.addAttribute(MESSAGE, "Veterinario actualizado correctamente");
			return this.showVetList(model);
		}
	}

}
