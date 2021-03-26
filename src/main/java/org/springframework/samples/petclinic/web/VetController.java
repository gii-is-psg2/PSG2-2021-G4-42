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

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	private static final String VIEWS_VETS_CREATE_OR_UPDATE_FORM = "vets/createOrUpdatePetForm";
	private static final String EDIT_VET = "vets/editVet";
	private final VetService vetService;

	@Autowired
	public VetController(VetService clinicService) {
		this.vetService = clinicService;
	}

	@GetMapping(value = { "/vets" })
	public String showVetList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of
		// Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
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
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}
	
	@GetMapping(value = "/vets/new")
	public String crearVet(ModelMap model) {
		String vista = "vets/editVet";
		model.addAttribute("vet", new Vet());
		return vista;
	}
	
	@PostMapping(value = "/vets/save")
	public String guardarVet(@Valid Vet vet, BindingResult result, ModelMap model) {
		String vista;
		if(result.hasErrors() ) {
			model.addAttribute("vet", vet);
			vista = "vets/editVet";
		} else {
			vetService.save(vet);
		
			model.addAttribute("message", "Veterinario creado satisfactoriamente");
			vista = showVetList(model);
		}
		return vista;
	}
	
	@GetMapping(value = "vets/update/{id}")
	public String editarVet(@PathVariable("id") int id, ModelMap model) {
		String vista = "vets/editVet";
		Optional<Vet> vet = vetService.findById(id);
		if(!vet.isPresent()) {
			model.addAttribute("message", "Veterinario no encontrado");
			vista = showVetList(model);
		} else {
			model.addAttribute("vet", vet.get());
		}
		return vista;
	}
	

//	@GetMapping(value = { "vets/edit/{id}" })
//	public String showUpdateForm(@PathVariable("id") int id, ModelMap model) {
//		Vet vet = vetService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
//		model.addAttribute("vet", vet);
//		return "update-vet";
//	}
//
//	@PostMapping(value = { "vets/update/{id}" })
//	public String updateVet(@PathVariable("id") int id, @Valid Vet vet, BindingResult result, ModelMap model) {
//		if (result.hasErrors()) {
//			vet.setId(id);
//			return "update-user";
//		}
//
//		vetService.save(vet);
//		return "redirect:/index";
//	}
//	
//	@GetMapping(value = "/vets/{id}/edit")
//	public String initUpdateForm(@PathVariable("id") int id, ModelMap model) {
//		Vet vet = this.vetService.findById(id).orElseThrow();
//		model.put("vet", vet);
//		return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
//	}
//	
//	@PostMapping(value = "/vets/{id}/edit")
//	public String processUpdateForm(@Valid Vet vet, BindingResult result,@PathVariable("id") int id, ModelMap model) {
//		if (result.hasErrors()) {
//			model.put("vet", vet);
//			return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
//		}
//		else {
//                        Vet vetToUpdate=vetService.findById(id).orElseThrow();
//			BeanUtils.copyProperties(vet, vetToUpdate, "id","owner","visits");                                                                                  
//                    try {                    
//                        this.vetService.save(vetToUpdate);                    
//                    } catch (Exception ex) {
//                        result.rejectValue("name", "duplicate", "already exists");
//                        return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
//                    }
//			return "redirect:/owners/{ownerId}";
//		}
//	}
}
