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
		final SolicitudAdopcion solicitudAdopcion = new SolicitudAdopcion();
		solicitudAdopcion.setAdopcion(this.adopcionService.findById(2).get());
		solicitudAdopcion.setFechaSolicitud(LocalDate.of(2021, 04, 20));
		solicitudAdopcion.setNuevoOwner(this.ownerService.findOwnerById(4));
		solicitudAdopcion.setSolicitud("Solicitud de prueba");

		this.solicitudAdopcionService.saveSolicitud(solicitudAdopcion);
		
		this.solicitudAdopcion = solicitudAdopcion;
	}

	@Test
	@Transactional
	void shouldFindNewSolicitudAdopciones() {
		Assert.assertEquals(this.solicitudAdopcion, this.solicitudAdopcionService.findSolicitudById(3).get());
	}

	@Test
	@Transactional
	void shouldUpdateSolicitudAdopcion() {
		final SolicitudAdopcion solicitudAdopcion1 = this.solicitudAdopcionService.findSolicitudById(1).get();
		solicitudAdopcion1.setAdopcion(this.adopcionService.findById(2).get());
		solicitudAdopcion1.setFechaSolicitud(LocalDate.of(2020, 04, 21));
		solicitudAdopcion1.setNuevoOwner(this.ownerService.findOwnerById(6));
		final String textoSolicitud = "Solicitud de prueba modificada";
		solicitudAdopcion1.setSolicitud(textoSolicitud);

		this.solicitudAdopcionService.saveSolicitud(solicitudAdopcion1);

		Assert.assertEquals(this.solicitudAdopcionService.findSolicitudById(1).get(), solicitudAdopcion1);
		Assert.assertTrue(solicitudAdopcion1.getFechaSolicitud().isEqual(LocalDate.of(2020, 04, 21)));
		Assert.assertEquals(solicitudAdopcion1.getNuevoOwner(), this.ownerService.findOwnerById(6));
		Assert.assertEquals(solicitudAdopcion1.getAdopcion(), this.adopcionService.findById(2).get());
		Assert.assertEquals(solicitudAdopcion1.getSolicitud(), textoSolicitud);
	}

	@Test
	@Transactional
	void shouldDeleteSolicitudAdopcion() {
		final Optional<SolicitudAdopcion> solicitudAdopcion = this.solicitudAdopcionService.findSolicitudById(1);
		
		this.solicitudAdopcionService.deleteSolicitud(solicitudAdopcion.get());
		
		Assert.assertFalse(this.solicitudAdopcionService.findSolicitudById(1).isPresent());
	}

}
