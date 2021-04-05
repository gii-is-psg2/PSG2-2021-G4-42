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

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
@RequestMapping("/visits")
public class VisitController {

	private final PetService petService;
	private final VisitService visitService;

	@Autowired
	public VisitController(final PetService petService, final VisitService visitService) {
		this.petService = petService;
		this.visitService=visitService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @GetMapping or @PostMapping annotated method. 2 goals:
	 * - Make sure we always have fresh data - Since we do not use the session scope, make
	 * sure that Pet object always has an id (Even though id is not part of the form
	 * fields)
	 * @param petId
	 * @return Pet
	 */
//	@ModelAttribute("visit")
//	public Visit loadPetWithVisit(@PathVariable("petId") int petId) {
//		Pet pet = this.petService.findPetById(petId);
//		Visit visit = new Visit();
//		pet.addVisit(visit);
//		return visit;
//	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping(value = "/{petId}/new")
	public String initNewVisitForm(@PathVariable final int petId, final ModelMap model) {
		final Visit v = new Visit();
		v.setPet(this.petService.findPetById(petId));
		model.addAttribute("visit", v);
		return "pets/createOrUpdateVisitForm";
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping(value = "/new")
	public String processNewVisitForm(@Valid final Visit visit, final BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		}
		else {
			this.petService.saveVisit(visit);
			return "redirect:/owners/{ownerId}";
		}
	}
	
	@GetMapping(value="/{visitId}/delete")
	public String deleteVisit(@PathVariable final int visitId, final ModelMap model) {
		final Optional<Visit> v = this.visitService.findById(visitId);
		
		if(!v.isPresent()) {
			model.addAttribute("message", "No existe la visita");
			model.addAttribute("messageType", "warning");
			return "welcome";
		}
		
		final Visit visit = v.get();
		final Pet pet = visit.getPet();
		
		final Owner owner = pet.getOwner();
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		final String rol = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().get().toString();
		final String nameOwner = owner.getUser().getUsername();

		if (username.equals(nameOwner)||rol.equals("admin")) {
			try {
				pet.removeVisit(visit);
				this.petService.savePet(pet);
				this.visitService.deleteVisit(visit);
			}catch(final Exception e) {
				model.put("message", e.getMessage());
			}
		}
		return "redirect:/owners/" + owner.getId();
	}
	

}
