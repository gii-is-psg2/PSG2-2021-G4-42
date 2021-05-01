package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
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
		this.owner = new Owner();
		this.owner.setId(1);
		this.owner.setFirstName("Prueba");
		this.owner.setLastName("de Owner");
		this.owner.setAddress("Calle de prueba");
		this.owner.setCity("Sevilla");
		this.owner.setTelephone("666555444");

		this.user = new User();
		this.user.setEnabled(true);
		this.user.setPassword("prueba");
		this.user.setUsername("owner");

		this.userService.saveUser(this.user);

		this.owner.setUser(this.user);
		this.ownerService.saveOwner(this.owner);

		this.owner2 = new Owner();
		this.owner2.setId(2);
		this.owner2.setFirstName("Prueba2");
		this.owner2.setLastName("de Owner");
		this.owner2.setAddress("Calle de prueba");
		this.owner2.setCity("Sevilla");
		this.owner2.setTelephone("666555444");

		this.user2 = new User();
		this.user2.setEnabled(true);
		this.user2.setPassword("pruebar");
		this.user2.setPassword("user2");

		this.userService.saveUser(this.user2);

		this.owner2.setUser(this.user2);
		this.ownerService.saveOwner(this.owner2);

		final PetType pety = new PetType();
		pety.setId(0);
		pety.setName("cat");

		this.pet = new Pet();
		this.pet.setBirthDate(LocalDate.of(2015, 01, 10));
		this.pet.setId(0);
		this.pet.setName("Toby123");
		this.pet.setOwner(this.owner);
		this.pet.setType(pety);
		
		this.petService.savePet(this.pet);

		this.sa = new SolicitudAdopcion();
		this.sa.setFechaSolicitud(LocalDate.of(2021, 02, 16));
		this.sa.setId(0);
		this.sa.setNuevoOwner(this.owner2);
		this.sa.setSolicitud("Solicitud de prueba");
		

		this.adop1 = new Adopcion();
		this.adop1.setId(AdopcionControllerTests.TEST_ADOPCION_ID);
		this.adop1.setFechaPuestaEnAdopcion(LocalDate.of(2021, 02, 15));
		this.adop1.setFechaResolucionAdopcion(LocalDate.of(2020, 03, 30));
		this.adop1.setPet(this.pet);
		this.adop1.setSolicitudAdopcion(Set.of(this.sa));
		
		this.adopcionService.save(this.adop1);
		
		this.sa.setAdopcion(this.adop1);
		this.solicitudAdopcionService.saveSolicitud(this.sa);
		this.adop2 = new Adopcion();
		this.adop2.setId(2);
		this.adop2.setFechaPuestaEnAdopcion(LocalDate.of(2021, 03, 15));
		this.adop2.setFechaResolucionAdopcion(LocalDate.of(2020, 03, 30));
		this.adop2.setPet(this.pet);
		this.adop2.setSolicitudAdopcion(this.solicitudAdopcionService.findSolicitudAdopcionByPetId(3));
		
		this.adopcionService.save(this.adop2);

		BDDMockito.given(this.adopcionService.findAll()).willReturn(Lists.newArrayList(this.adop1, this.adop2));
		BDDMockito.given(this.adopcionService.findById(AdopcionControllerTests.TEST_ADOPCION_ID)).willReturn(Optional.of(this.adop1));
		BDDMockito.given(this.adopcionService.findAdopcionByIdPetId(0)).willReturn(this.adop1);
		BDDMockito.given(this.ownerService.findOwnerById(this.owner.getId())).willReturn(this.owner);
		BDDMockito.given(this.ownerService.findOwnerById(this.owner2.getId())).willReturn(this.owner2);
		BDDMockito.given(this.ownerService.findOwnerByUsername(this.owner.getUser().getUsername())).willReturn(Optional.of(this.owner));
		BDDMockito.given(this.petService.findPetById(0)).willReturn(this.pet);
		BDDMockito.given(this.petService.findPetsByOwner("owner")).willReturn(List.of(this.pet));
		BDDMockito.given(this.solicitudAdopcionService.findSolicitudById(0)).willReturn(Optional.of(this.sa));
		BDDMockito.given(this.solicitudAdopcionService.findSolicitudAdopcionByPetId(0)).willReturn(Set.of(this.sa));

	}

	@WithMockUser(value = "spring")
	@Test
	void testAdopcionNotOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adopciones"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("adopciones"))
				.andExpect(MockMvcResultMatchers.view().name("welcome"));
	}

	@WithMockUser(value = "spring", authorities = "owner", username = "owner")
	@Test
	void testAdopcionOwner() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/adopciones"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("adopciones"))
				.andExpect(MockMvcResultMatchers.view().name(AdopcionController.VIEWS_ADOPCION_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNewAdopcionGet() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/adopciones/{petId}/new", 2, 0))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("pet", Matchers.hasProperty("name", Matchers.is("Toby123"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("firstName", Matchers.is("Prueba2"))))
				.andExpect(MockMvcResultMatchers.model().attribute("fecha", LocalDate.now()))
				.andExpect(MockMvcResultMatchers.view().name(AdopcionController.VIEWS_ADOPCION_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNewAdopcionPost() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/adopciones/{petId}/new", 2, 0).with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isFound());
	}

	@WithMockUser(value = "spring", authorities = "admin")
	@Test
	void testDeleteAdopcion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adopciones/delete/{adopcionId}", AdopcionControllerTests.TEST_ADOPCION_ID))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/1"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSolicitudAdopcionGet() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adopciones/solicitud/{adopcionId}", AdopcionControllerTests.TEST_ADOPCION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("welcome"));
	}

	@WithMockUser(value = "spring", authorities = "owner", username = "owner")
	@Test
	void testSolicitudAdopcionPost() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/adopciones/solicitud/{adopcionId}", AdopcionControllerTests.TEST_ADOPCION_ID)
						.param("id", "1")
						.param("fechaSolicitud", "2021-02-16")
						.param("nuevoOwner.id", "1")
						.param("solicitud", "Solicitud de prueba")
						.param("adopcion.id", "1")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.model().attribute("message", "La solicitud se ha creado correctamente "))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name(AdopcionController.VIEWS_ADOPCION_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInteresadosAdopcionGet() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adopciones/interesados/{petId}", 0))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("solicitudes"))
				.andExpect(MockMvcResultMatchers.view().name(AdopcionController.VIEWS_ADOPCION_INTERESADOS_FORM));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInteresadosAdopcionAceptarGet() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adopciones/solicitudAdopcion/{solicitudId}/aceptar", 0))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/" + this.owner.getId()));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInteresadosAdopcionDenegarGet() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adopciones/solicitudAdopcion/{solicitudId}/denegar", 0))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/" + this.owner.getId()));
	}
}
