package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.stereotype.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ReservaServiceTests {
	
	@Autowired
	PetService petService;
	
	@Autowired
	ReservaService reservaService;
	
	@Autowired
	HabitacionService habitacionService;
	
	@Test
	public void shouldInsertReserva() throws Exception {
		Collection<Reserva> reservas = this.reservaService.findAll();
		int found = reservas.size();
		Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.of(2022, 10, 12));
		reserva.setFechaFin(LocalDate.of(2022, 10, 16));
		reserva.setHabitacion(habitacionService.findHabitacionByNumero(1).get());
		reserva.setPet(petService.findPetById(10));
		
		this.reservaService.save(reserva);
		reservas = this.reservaService.findAll();
		assertThat(reservas.size()).isEqualTo(found+1);
		
	}
	
	@Test
	public void shouldNotInsertReservaMascotaOcupada() {
		Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.now());
		reserva.setFechaFin(LocalDate.now());
		reserva.setHabitacion(habitacionService.findHabitacionByNumero(15).get());
		reserva.setPet(petService.findPetById(1));
		
		Assertions.assertThrows(Exception.class, ()->{
			this.reservaService.save(reserva);
		});
		
	}
	
	@Test 
	public void shouldNotInsertReservaConHabitacionOcupada() {
		Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.now());
		reserva.setFechaFin(LocalDate.now());
		reserva.setHabitacion(habitacionService.findHabitacionByNumero(4).get());
		reserva.setPet(petService.findPetById(10));
		
		Assertions.assertThrows(Exception.class, ()->{
			this.reservaService.save(reserva);
		});
		
	}
	
	@Test 
	public void shouldNotInsertReservaConFechasIncorrectas() {
		Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.of(2021, 10, 15));
		reserva.setFechaFin(LocalDate.now());
		reserva.setHabitacion(habitacionService.findHabitacionByNumero(15).get());
		reserva.setPet(petService.findPetById(10));
		
		Assertions.assertThrows(Exception.class, ()->{
			this.reservaService.save(reserva);
		});
		
	}
	
	@Test
	public void shouldNotInsertReservaConFechasIncorrectas2() {
		Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.of(2020, 10, 10));
		reserva.setFechaFin(LocalDate.of(2020, 10, 23));
		reserva.setHabitacion(habitacionService.findHabitacionByNumero(15).get());
		reserva.setPet(petService.findPetById(10));
		
		Assertions.assertThrows(Exception.class, ()-> {
			this.reservaService.save(reserva);
		});
	}
	
	
	

}
