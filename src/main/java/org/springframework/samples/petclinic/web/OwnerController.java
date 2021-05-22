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

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.AdopcionService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.web.exceptions.OwnerNoEncontradoException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
public class OwnerController {

	public static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
	public static final String ADMIN = "admin";
	
	private final OwnerService ownerService;
	
	@Autowired
	ReservaService reservaService;
	@Autowired
	AdopcionService adopcionService;
	
	@Autowired
	public OwnerController(final OwnerService ownerService, final UserService userService, final AuthoritiesService authoritiesService) {
		this.ownerService = ownerService;
	}
	
	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/owners/find")
	public String initFindForm(final Map<String, Object> model) {
		model.put("owner", new Owner());
		model.put("owners", ownerService.findAll());
		return "owners/findOwners";
	}

	@GetMapping(value = "/owners")
	public String processFindForm(Owner owner, final BindingResult result, final Map<String, Object> model) {

		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) {
			owner.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		final Collection<Owner> results = this.ownerService.findOwnerByLastName(owner.getLastName());
		if (results.isEmpty()) {
			// no owners found
			result.rejectValue("lastName", "notFound", "not found");
			model.put("owners", ownerService.findAll());
			return "owners/findOwners";
		}
		else if (results.size() == 1) {
			// 1 owner found
			owner = results.iterator().next();
			return "redirect:/owners/" + owner.getId();
		}
		else if(results.size() == ownerService.findAll().size()) {
			result.rejectValue("lastName", "Escribe un apellido", "Escribe un apellido");
			model.put("owners", ownerService.findAll());
			return "owners/findOwners";
			
		}
		else {
			model.put("selections", results);
			return "owners/ownersList";
		}
	}

	@GetMapping(value = "/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") final int ownerId, final ModelMap model) {
		final Owner owner = this.ownerService.findOwnerById(ownerId);
		model.addAttribute(owner);
		
		final Optional<? extends GrantedAuthority> rolOptional = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst();
		String rol = "";
		if(rolOptional.isPresent()) {
			rol = rolOptional.get().getAuthority();
		}
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(!username.equals(owner.getUser().getUsername()) || !rol.equals(ADMIN)) {
			return this.showOwner(model, ownerId);
		}
		
		return OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid final Owner owner, final BindingResult result,
			@PathVariable("ownerId") final int ownerId) {
		if (result.hasErrors()) {
			return OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		else {
			owner.setId(ownerId);
			this.ownerService.saveOwner(owner);
			return "redirect:/owners/{ownerId}";
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/owners/{ownerId}")
	public String showOwner(final ModelMap model, @PathVariable("ownerId") final int ownerId) {
		model.addAttribute("reservas", this.reservaService.findReservasByOwner(ownerId));
		model.addAttribute("adopciones", this.adopcionService.findAdopcionByIdOwnerId(ownerId));
		final Owner owner = this.ownerService.findOwnerById(ownerId);
		model.addAttribute("owner", owner);
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		final Optional<? extends GrantedAuthority> rolOptional = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst();
		String rol = "";
		if(rolOptional.isPresent()) {
			rol = rolOptional.get().getAuthority();
		}
		
		model.addAttribute("showButtons", (rol.equals(ADMIN) || username.equals(owner.getUser().getUsername())));
		
		return "owners/ownerDetails";
	}
	
	@GetMapping("/owners/profile")
	public String showProfile(final ModelMap model, Principal principal) {
		Optional<Owner> owner = ownerService.findOwnerByUsername(principal.getName());
		if(owner.isPresent()) {
			return showOwner(model, owner.get().getId());
		}
		return initFindForm(model);
	}
	
	@GetMapping(value="/owners/{ownerId}/delete")
	public String deleteOwner(@PathVariable("ownerId") final int ownerId, final Map<String, Object> model) throws OwnerNoEncontradoException {
		final Owner owner = this.ownerService.findOwnerById(ownerId);
		final String username = owner.getUser().getUsername();
		final Optional<? extends GrantedAuthority> rolOptional = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst();
		String rol = "";
		if(rolOptional.isPresent()) {
			rol = rolOptional.get().toString();
		}
		final String username2 = SecurityContextHolder.getContext().getAuthentication().getName();
		if(rol.equals(ADMIN) && !username.equals(username2)) {
			try {
				this.ownerService.deleteOwner(owner);
			}catch(final Exception e) {
				throw new OwnerNoEncontradoException();
			}

			final Collection<Owner> results = this.ownerService.findOwnerByLastName(""); 
			model.put("selections", results);
			return "owners/ownersList";
		}else {
			return "redirect:/";
		}
	}
		
}
