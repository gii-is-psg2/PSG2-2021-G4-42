package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Adopcion;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class AdopcionServiceTests {

	@Autowired
	private AdopcionService adopcionService;

	@Autowired
	private PetService petService;

	@Autowired
	private SolicitudAdopcionService solicitudAdopcionService;

	public Adopcion adopcion;

	@BeforeEach
	void insertarAdopcion() throws Exception {
		Adopcion adopcion = new Adopcion();

		adopcion.setFechaPuestaEnAdopcion(LocalDate.of(2021, 4, 10));
		adopcion.setFechaResolucionAdopcion(LocalDate.of(2021, 4, 15));
		adopcion.setPet(petService.findPetById(1));
		adopcion.setSolicitudAdopcion(solicitudAdopcionService.findSolicitudAdopcionByPetId(1));

		adopcionService.save(adopcion);
		this.adopcion = adopcion;
	}

	@Test
	void ShouldFindAdopcion() {
		final Collection<Adopcion> adopciones = this.adopcionService.findAll();

		final Adopcion adopcion = EntityUtils.getById(adopciones, Adopcion.class, 2);
		Assert.assertTrue(adopcion.getFechaPuestaEnAdopcion().isEqual(LocalDate.of(2021, 1, 12)));
		Assert.assertTrue(adopcion.getFechaResolucionAdopcion().isEqual(LocalDate.of(2021, 1, 20)));
		Assert.assertTrue(adopcion.getPet().equals(petService.findPetById(3)));
	}

	@Test
	@Transactional
	void shouldAddNewAdopcion() throws Exception {
		assertEquals(adopcion, adopcionService.findAdopcionByIdPetId(1));
	}

	@Test
	@Transactional
	void ShouldUpdateAdopcion() throws Exception {
		Adopcion adopcion1 = this.adopcionService.findAdopcionByIdPetId(1);
		adopcion1.setFechaPuestaEnAdopcion(LocalDate.of(2020, 03, 10));
		adopcion1.setFechaResolucionAdopcion(LocalDate.of(2020, 03, 15));
		adopcion1.setPet(petService.findPetById(4));
		adopcion1.setSolicitudAdopcion(solicitudAdopcionService.findSolicitudAdopcionByPetId(4));

		adopcionService.save(adopcion1);

		Assert.assertTrue(adopcionService.findAdopcionByIdPetId(4).equals(adopcion1));
		Assert.assertTrue(adopcion1.getFechaPuestaEnAdopcion().isEqual(LocalDate.of(2020, 03, 10)));
		Assert.assertTrue(adopcion1.getFechaResolucionAdopcion().isEqual(LocalDate.of(2020, 03, 15)));
		Assert.assertTrue(
				adopcion1.getSolicitudAdopcion().equals(solicitudAdopcionService.findSolicitudAdopcionByPetId(4)));
	}

	@Test
	@Transactional
	void ShouldDeleteAdopcion() {
		final Optional<Adopcion> adopcion = this.adopcionService.findById(1);

		this.adopcionService.delete(adopcion.get());

		Assert.assertFalse(this.adopcionService.findById(1).isPresent());
	}
}
