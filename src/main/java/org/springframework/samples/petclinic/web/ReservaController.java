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
import org.springframework.samples.petclinic.model.Habitacion;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.service.HabitacionService;
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
	
	@Autowired
	private HabitacionService habitacionService;
	
	@GetMapping("")
	public String reservas(ModelMap model) {
		model.addAttribute("reservas", reservaService.findAll());
		return "/reserva/reservaList";
		
	}
	
	@GetMapping(value="/new")
	public String newReserva(final ModelMap model) {
		model.addAttribute("reserva", new Reserva());
		return ReservaController.VIEWS_RESERVA_FECHA_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value="/new")
	public String newReservaPost(@Valid final Reserva reserva, final BindingResult result, final ModelMap model) {
		if(result.hasErrors()) {
			this.addModelData(model, reserva);
			return ReservaController.VIEWS_RESERVA_CREATE_OR_UPDATE_FORM;
		}
		try {
			this.reservaService.save(reserva);
		}catch (final Exception e) {
			model.addAttribute("message", e.getMessage());
			model.addAttribute("messageType", "danger");
			this.addModelData(model, reserva);
			return ReservaController.VIEWS_RESERVA_CREATE_OR_UPDATE_FORM;
		}
		model.addAttribute("message", String.format("Habitación nº %d reservada correctamente", reserva.getHabitacion().getNumero()));
		return "welcome";
	}
	
	@PostMapping(value="/new/fechas")
	public String newReservaFechas(@RequestParam("fechaIni") final String fechaIni, @RequestParam("fechaFin") final String fechaFin, final ModelMap model){
		final Reserva reserva = new Reserva();
		
		final LocalDate fechaIni1 = LocalDate.parse(fechaIni, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		final LocalDate fechaFin1 = LocalDate.parse(fechaFin, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			
		reserva.setFechaIni(fechaIni1);
		reserva.setFechaFin(fechaFin1);

		this.addModelData(model, reserva);
		
		return ReservaController.VIEWS_RESERVA_CREATE_OR_UPDATE_FORM;
	}
	
//	@GetMapping(value="")
//	public String deleteReserva() {
//		
//	}
	
	public void addModelData(final ModelMap model, final Reserva reserva) {

		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		final List<Pet> pets = this.petService.findPetsByOwner(username);
		
		model.addAttribute("pets", pets);
		
		final List<Habitacion> habitaciones = this.habitacionService.findAll();
		model.addAttribute("habitaciones", habitaciones);
		model.addAttribute("reserva", reserva);
	}
}
