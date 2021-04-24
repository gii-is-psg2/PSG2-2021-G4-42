package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ReservaServiceTests {
	
	@Autowired
	PetService petService;
	
	@Autowired
	ReservaService reservaService;
	
	@Autowired
	HabitacionService habitacionService;
	
	@Test
	@Transactional
	public void shouldInsertReserva() throws Exception {
		Collection<Reserva> reservas = this.reservaService.findAll();
		final int found = reservas.size();
		final Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.of(2022, 10, 12));
		reserva.setFechaFin(LocalDate.of(2022, 10, 16));
		reserva.setHabitacion(this.habitacionService.findHabitacionByNumero(1).get());
		reserva.setPet(this.petService.findPetById(10));
		
		this.reservaService.save(reserva);
		reservas = this.reservaService.findAll();
		assertEquals(reservas.size(), found+1);
		
	}
	
	@Test
	@Transactional
	public void shouldUpdateReserva() throws Exception{
		
		final Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.now().plusYears(10));
		reserva.setFechaFin(LocalDate.now().plusYears(10).plusDays(5));
		reserva.setHabitacion(this.habitacionService.findHabitacionByNumero(1).get());
		reserva.setPet(this.petService.findPetById(10));
		this.reservaService.save(reserva);
		
		Reserva reserva2=this.reservaService.findByFechaIni(LocalDate.now().plusYears(10));
		int id = reserva2.getId();
		reserva2.setFechaFin(LocalDate.now().plusYears(10).plusDays(10));
		this.reservaService.save(reserva2);
		
		assertEquals(this.reservaService.findById(id).get().getFechaFin(), LocalDate.now().plusYears(10).plusDays(10));
		
	}
	
	@Test
	public void shouldNotSaveReservaMascotaOcupada() {
		final Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.now());
		reserva.setFechaFin(LocalDate.now());
		reserva.setHabitacion(this.habitacionService.findHabitacionByNumero(15).get());
		reserva.setPet(this.petService.findPetById(4));
		
		Assertions.assertThrows(Exception.class, ()->{
			this.reservaService.save(reserva);
		});
		
	}
	
	@Test 
	public void shouldNotSaveReservaConHabitacionOcupada() {
		final Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.now());
		reserva.setFechaFin(LocalDate.now());
		reserva.setHabitacion(this.habitacionService.findHabitacionByNumero(4).get());
		reserva.setPet(this.petService.findPetById(10));
		
		Assertions.assertThrows(Exception.class, ()->{
			this.reservaService.save(reserva);
		});
		
	}
	
	@Test 
	public void shouldNotSaveReservaConFechasIncorrectasIniMayorQueHoy() {
		final Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.now().plusYears(3));
		reserva.setFechaFin(LocalDate.now());
		reserva.setHabitacion(this.habitacionService.findHabitacionByNumero(15).get());
		reserva.setPet(this.petService.findPetById(10));
		
		Assertions.assertThrows(Exception.class, ()->{
			this.reservaService.save(reserva);
		});
		
	}
	
	@Test
	public void shouldNotSaveReservaConFechasIncorrectasPasadas() {
		final Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.of(2020, 10, 10));
		reserva.setFechaFin(LocalDate.of(2020, 10, 23));
		reserva.setHabitacion(this.habitacionService.findHabitacionByNumero(15).get());
		reserva.setPet(this.petService.findPetById(10));
		
		Assertions.assertThrows(Exception.class, ()-> {
			this.reservaService.save(reserva);
		});
	}
	
	@Test
	public void shouldDeleteReserva() throws Exception {
		final Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.of(2100, 10, 15));
		reserva.setFechaFin(LocalDate.of(2100, 10, 16));
		reserva.setHabitacion(this.habitacionService.findHabitacionByNumero(15).get());
		reserva.setPet(this.petService.findPetById(10));
		reserva.setId(1);
		
		this.reservaService.save(reserva);
		
		assertTrue(this.reservaService.findById(1).isPresent());
		
		this.reservaService.delete(reserva);

		assertFalse(this.reservaService.findById(1).isPresent());
	}
	

}
