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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/reserva")
public class ReservaController {

	private static final String VIEWS_RESERVA_FECHA_CREATE_OR_UPDATE_FORM = "reserva/createOrUpdateReservaFechaForm";
	private static final String VIEWS_RESERVA_CREATE_OR_UPDATE_FORM = "reserva/createOrUpdateReservaForm";
	
	@Autowired
	private PetService petService;
	
	@Autowired
	private ReservaService reservaService;
	
	@GetMapping(value="/new")
	public String newReserva(final ModelMap model) {
		model.addAttribute("reserva", new Reserva());
		return ReservaController.VIEWS_RESERVA_FECHA_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value="/new")
	public String newReservaPost(@Valid final Reserva reserva, final ModelMap model, final BindingResult result) {
		if(result.hasErrors()) {
			model.addAttribute("reserva", reserva);
			return ReservaController.VIEWS_RESERVA_CREATE_OR_UPDATE_FORM;
		}
		return "";
	}
	
	@PostMapping(value="/new/fechas")
	public String newReservaFechas(@RequestParam("fechaIni") final String fechaIni, @RequestParam("fechaFin") final String fechaFin, final ModelMap model){
		final Reserva r = new Reserva();
		
		final LocalDate fechaIni1 = LocalDate.parse(fechaIni, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		final LocalDate fechaFin1 = LocalDate.parse(fechaFin, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			
		r.setFechaIni(fechaIni1);
		r.setFechaFin(fechaFin1);

		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		final List<Pet> pets = this.petService.findPetsByOwner(username);
		final List<Pet> petsConReserva = this.reservaService.findPetReservaBetweenFechasAndUsername(fechaIni1, fechaFin1, username);
		
		pets.removeAll(petsConReserva);
		
		model.addAttribute("pets", pets);
		
		model.addAttribute("reserva", r);
		
		return ReservaController.VIEWS_RESERVA_CREATE_OR_UPDATE_FORM;
	}

}
