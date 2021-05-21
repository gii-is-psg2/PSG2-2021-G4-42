package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Causa;
import org.springframework.samples.petclinic.model.Donacion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.DonacionRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class CausaServiceTests {

	@Autowired
	private CausaService causaService;
	
	@Autowired
	private DonacionRepository donacionRepository; // Cambiarlo si está el servicio
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private UserService userService;
	
	private Causa causa;
	private Donacion donacion1, donacion2;
	
	@BeforeEach
	void setup() throws Exception {
		this.causa = new Causa();
		this.causa.setId(1);
		this.causa.setNombre("Nombre");
		this.causa.setDescripcion("Descripcion");
		this.causa.setOrganizacion("Organización");
		this.causa.setRecaudacionObjetivo(100.);
		this.causaService.save(this.causa);
		
		final User u = new User();
		u.setEnabled(true);
		u.setPassword("contraseña");
		u.setUsername("username");
		this.userService.saveUser(u);
		
		final Owner owner = new Owner();
		owner.setAddress("direccion");
		owner.setCity("ciudad");
		owner.setFirstName("nombre");
		owner.setLastName("apellidos");
		owner.setId(1);
		owner.setTelephone("123456789");
		owner.setUser(u);
		this.ownerService.saveOwner(owner);
		
		this.donacion1 = new Donacion();
		this.donacion1.setId(1);
		this.donacion1.setCantidadDonada(10.);
		this.donacion1.setCausa(this.causa);
		this.donacion1.setDonante(owner);
		this.donacion1.setFechaDonacion(LocalDate.now());
		this.donacionRepository.save(this.donacion1);
		
		this.donacion2 = new Donacion();
		this.donacion2.setId(2);
		this.donacion2.setCantidadDonada(10.);
		this.donacion2.setCausa(this.causa);
		this.donacion2.setDonante(owner);
		this.donacion2.setFechaDonacion(LocalDate.now());
		this.donacionRepository.save(this.donacion2);
	}
	
	@Test
	void shouldFindDonacionesByCausa() {
		final Collection<Donacion> donaciones = this.causaService.findDonacionesByCausa(this.causa.getId());
		
		Assert.assertTrue(donaciones.contains(this.donacion1));
		Assert.assertTrue(donaciones.contains(this.donacion2));
	}
}
