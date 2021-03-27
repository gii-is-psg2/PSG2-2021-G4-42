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
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class VisitController {

	private final PetService petService;
	private final VisitService visitService;

	@Autowired
	public VisitController(PetService petService, VisitService visitService) {
		this.petService = petService;
		this.visitService=visitService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
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
	@GetMapping(value = "/owners/*/pets/{petId}/visits/new")
	public String initNewVisitForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "pets/createOrUpdateVisitForm";
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		}
		else {
			this.petService.saveVisit(visit);
			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/owners/*/pets/{petId}/visits")
	public String showVisits(@PathVariable int petId, Map<String, Object> model) {
		model.put("visits", this.petService.findPetById(petId).getVisits());
		return "visitList";
	}
	
	@GetMapping(value="/owners/{ownerId}/pets/{petId}/visits/{visitId}/delete")
	public String deleteVisit(@PathVariable int petId, @PathVariable int ownerId, @PathVariable int visitId, Map<String, Object> model) {
		final Visit v = this.visitService.findById(visitId).get();
		final Pet pet = this.petService.findPetById(petId);
		final Owner owner = pet.getOwner();
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		final String rol = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().get().toString();
		final String nameOwner = owner.getUser().getUsername();
		List<Visit> visits = this.petService.findVisitsByPetId(petId).stream().collect(Collectors.toList());
		if (username.equals(nameOwner)||rol.equals("admin") && visits.contains(v)) {
			try {
				pet.removeVisit(v);
				this.petService.savePet(pet);
				this.visitService.deleteVisit(v);
			}catch(Exception e) {
				model.put("message", e.getMessage());
			}
		}
		return "redirect:/owners/" + owner.getId();
	}
	

}
