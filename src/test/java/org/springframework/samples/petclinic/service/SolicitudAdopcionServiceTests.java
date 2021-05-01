package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.SolicitudAdopcion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class SolicitudAdopcionServiceTests {

	@Autowired
	protected SolicitudAdopcionService solicitudAdopcionService;

	@Autowired
	protected AdopcionService adopcionService;

	@Autowired
	protected OwnerService ownerService;

	public SolicitudAdopcion solicitudAdopcion;

	@BeforeEach
	void insertarSolicitudAdopcion() throws Exception {
		SolicitudAdopcion solicitudAdopcion = new SolicitudAdopcion();
		solicitudAdopcion.setAdopcion(adopcionService.findById(2).get());
		solicitudAdopcion.setFechaSolicitud(LocalDate.of(2021, 04, 20));
		solicitudAdopcion.setNuevoOwner(ownerService.findOwnerById(4));
		solicitudAdopcion.setSolicitud("Solicitud de prueba");

		solicitudAdopcionService.saveSolicitud(solicitudAdopcion);
		
		this.solicitudAdopcion = solicitudAdopcion;
	}

	@Test
	@Transactional
	void shouldFindNewSolicitudAdopciones() {
		Assert.assertTrue(solicitudAdopcion.equals(solicitudAdopcionService.findSolicitudById(3).get()));
	}

	@Test
	@Transactional
	void shouldUpdateSolicitudAdopcion() {
		SolicitudAdopcion solicitudAdopcion1 = this.solicitudAdopcionService.findSolicitudById(1).get();
		solicitudAdopcion1.setAdopcion(adopcionService.findById(2).get());
		solicitudAdopcion1.setFechaSolicitud(LocalDate.of(2020, 04, 21));
		solicitudAdopcion1.setNuevoOwner(ownerService.findOwnerById(6));
		solicitudAdopcion1.setSolicitud("Solicitud de prueba modificada");

		solicitudAdopcionService.saveSolicitud(solicitudAdopcion1);

		Assert.assertTrue(solicitudAdopcionService.findSolicitudById(1).get().equals(solicitudAdopcion1));
		Assert.assertTrue(solicitudAdopcion1.getFechaSolicitud().isEqual(LocalDate.of(2020, 04, 21)));
		Assert.assertTrue(solicitudAdopcion1.getNuevoOwner().equals(ownerService.findOwnerById(6)));
		Assert.assertTrue(solicitudAdopcion1.getAdopcion().equals(adopcionService.findById(2).get()));
		Assert.assertTrue(solicitudAdopcion1.getSolicitud().equals("Solicitud de prueba modificada"));
	}

	@Test
	@Transactional
	void shouldDeleteSolicitudAdopcion() {
		final Optional<SolicitudAdopcion> solicitudAdopcion = this.solicitudAdopcionService.findSolicitudById(1);
		
		this.solicitudAdopcionService.deleteSolicitud(solicitudAdopcion.get());
		
		Assert.assertFalse(this.solicitudAdopcionService.findSolicitudById(1).isPresent());
	}

}
