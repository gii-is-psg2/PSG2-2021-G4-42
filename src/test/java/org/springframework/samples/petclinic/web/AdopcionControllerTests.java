package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Adopcion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.SolicitudAdopcion;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AdopcionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SolicitudAdopcionService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AdopcionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class AdopcionControllerTests {
	private static final int TEST_ADOPCION_ID = 1;

	@Autowired
	private AdopcionController adopcionController;

	@MockBean
	private AdopcionService adopcionService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SolicitudAdopcionService solicitudAdopcionService;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private PetService petService;

	@MockBean
	private UserService userService;

	private Adopcion adop1;
	private Adopcion adop2;
	private Owner owner;
	private User user;
	private Owner owner2;
	private User user2;
	private SolicitudAdopcion sa;
	private Pet pet;

	@BeforeEach
	void setup() throws Exception {
		owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Prueba");
		owner.setLastName("de Owner");
		owner.setAddress("Calle de prueba");
		owner.setCity("Sevilla");
		owner.setTelephone("666555444");

		user = new User();
		user.setEnabled(true);
		user.setPassword("prueba");
		user.setUsername("owner");

		userService.saveUser(user);

		owner.setUser(user);
		ownerService.saveOwner(owner);

		owner2 = new Owner();
		owner2.setId(2);
		owner2.setFirstName("Prueba2");
		owner2.setLastName("de Owner");
		owner2.setAddress("Calle de prueba");
		owner2.setCity("Sevilla");
		owner2.setTelephone("666555444");

		user2 = new User();
		user2.setEnabled(true);
		user2.setPassword("pruebar");
		user2.setPassword("user2");

		userService.saveUser(user2);

		owner2.setUser(user2);
		ownerService.saveOwner(owner2);

		PetType pety = new PetType();
		pety.setId(0);
		pety.setName("cat");

		pet = new Pet();
		pet.setBirthDate(LocalDate.of(2015, 01, 10));
		pet.setId(0);
		pet.setName("Toby123");
		pet.setOwner(owner);
		pet.setType(pety);
		
		petService.savePet(pet);

		sa = new SolicitudAdopcion();
		sa.setFechaSolicitud(LocalDate.of(2021, 02, 16));
		sa.setId(0);
		sa.setNuevoOwner(owner2);
		sa.setSolicitud("Solicitud de prueba");
		

		adop1 = new Adopcion();
		adop1.setId(TEST_ADOPCION_ID);
		adop1.setFechaPuestaEnAdopcion(LocalDate.of(2021, 02, 15));
		adop1.setFechaResolucionAdopcion(LocalDate.of(2020, 03, 30));
		adop1.setPet(pet);
		adop1.setSolicitudAdopcion(Set.of(sa));
		
		adopcionService.save(adop1);
		
		sa.setAdopcion(adop1);
		solicitudAdopcionService.saveSolicitud(sa);
		adop2 = new Adopcion();
		adop2.setId(2);
		adop2.setFechaPuestaEnAdopcion(LocalDate.of(2021, 03, 15));
		adop2.setFechaResolucionAdopcion(LocalDate.of(2020, 03, 30));
		adop2.setPet(pet);
		adop2.setSolicitudAdopcion(solicitudAdopcionService.findSolicitudAdopcionByPetId(3));
		
		adopcionService.save(adop2);

		given(this.adopcionService.findAll()).willReturn(Lists.newArrayList(adop1, adop2));
		given(this.adopcionService.findById(TEST_ADOPCION_ID)).willReturn(Optional.of(adop1));
		given(this.adopcionService.findAdopcionByIdPetId(0)).willReturn(adop1);
		given(this.ownerService.findOwnerById(owner.getId())).willReturn(owner);
		given(this.ownerService.findOwnerById(owner2.getId())).willReturn(owner2);
		given(this.ownerService.findOwnerByUsername(owner.getUser().getUsername())).willReturn(Optional.of(owner));
		given(this.petService.findPetById(0)).willReturn(pet);
		given(this.petService.findPetsByOwner("owner")).willReturn(List.of(pet));
		given(this.solicitudAdopcionService.findSolicitudById(0)).willReturn(Optional.of(sa));
		given(this.solicitudAdopcionService.findSolicitudAdopcionByPetId(0)).willReturn(Set.of(sa));

	}

	@WithMockUser(value = "spring")
	@Test
	void testAdopcionNotOwner() throws Exception {
		mockMvc.perform(get("/adopciones"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("adopciones"))
				.andExpect(view().name("welcome"));
	}

	@WithMockUser(value = "spring", authorities = "owner", username = "owner")
	@Test
	void testAdopcionOwner() throws Exception {

		mockMvc.perform(get("/adopciones"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("adopciones"))
				.andExpect(view().name(AdopcionController.VIEWS_ADOPCION_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNewAdopcionGet() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/adopciones/{petId}/new", 2, 0))
				.andExpect(status().isOk())
				.andExpect(model().attribute("pet", hasProperty("name", is("Toby123"))))
				.andExpect(model().attribute("owner", hasProperty("firstName", is("Prueba2"))))
				.andExpect(model().attribute("fecha", LocalDate.now()))
				.andExpect(view().name(AdopcionController.VIEWS_ADOPCION_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNewAdopcionPost() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/adopciones/{petId}/new", 2, 0).with(csrf()))
				.andExpect(status().isFound());
	}

	@WithMockUser(value = "spring", authorities = "admin")
	@Test
	void testDeleteAdopcion() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/adopciones/delete/{adopcionId}", TEST_ADOPCION_ID))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/1"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSolicitudAdopcionGet() throws Exception {
		mockMvc.perform(get("/adopciones/solicitud/{adopcionId}", TEST_ADOPCION_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("welcome"));
	}

	@WithMockUser(value = "spring", authorities = "owner", username = "owner")
	@Test
	void testSolicitudAdopcionPost() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/adopciones/solicitud/{adopcionId}", TEST_ADOPCION_ID)
						.param("id", "1")
						.param("fechaSolicitud", "2021-02-16")
						.param("nuevoOwner.id", "1")
						.param("solicitud", "Solicitud de prueba")
						.param("adopcion.id", "1")
						.with(csrf()))
				.andExpect(model().attribute("message", "La solicitud se ha creado correctamente "))
				.andExpect(status().isOk())
				.andExpect(view().name(AdopcionController.VIEWS_ADOPCION_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInteresadosAdopcionGet() throws Exception {
		mockMvc.perform(get("/adopciones/interesados/{petId}", 0))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("solicitudes"))
				.andExpect(view().name(AdopcionController.VIEWS_ADOPCION_INTERESADOS_FORM));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInteresadosAdopcionAceptarGet() throws Exception {
		mockMvc.perform(get("/adopciones/solicitudAdopcion/{solicitudId}/aceptar", 0))
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/owners/" + owner.getId()));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInteresadosAdopcionDenegarGet() throws Exception {
		mockMvc.perform(get("/adopciones/solicitudAdopcion/{solicitudId}/denegar", 0))
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/owners/" + owner.getId()));
	}
}
