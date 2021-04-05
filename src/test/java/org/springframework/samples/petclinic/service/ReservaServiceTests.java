package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.Assert;
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
	
	// Pasar la inicialización de reserva a una función con la anotación @BeforeEach
	@Test
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
		org.assertj.core.api.Assertions.assertThat(reservas.size()).isEqualTo(found+1);
		
	}
	
	@Test
	public void shouldNotInsertReservaMascotaOcupada() {
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
	public void shouldNotInsertReservaConHabitacionOcupada() {
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
	public void shouldNotInsertReservaConFechasIncorrectas() {
		final Reserva reserva = new Reserva();
		reserva.setFechaIni(LocalDate.of(2021, 10, 15));
		reserva.setFechaFin(LocalDate.now());
		reserva.setHabitacion(this.habitacionService.findHabitacionByNumero(15).get());
		reserva.setPet(this.petService.findPetById(10));
		
		Assertions.assertThrows(Exception.class, ()->{
			this.reservaService.save(reserva);
		});
		
	}
	
	@Test
	public void shouldNotInsertReservaConFechasIncorrectas2() {
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
		
		Assert.assertTrue(this.reservaService.findById(1).isPresent());
		
		this.reservaService.delete(reserva);

		Assert.assertFalse(this.reservaService.findById(1).isPresent());
	}
	

}
