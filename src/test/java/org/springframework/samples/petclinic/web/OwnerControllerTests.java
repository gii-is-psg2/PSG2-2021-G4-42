package org.springframework.samples.petclinic.web;

import java.util.ArrayList;

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
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AdopcionService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers=OwnerController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class OwnerControllerTests {

	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private OwnerController ownerController;

	@MockBean
	private OwnerService clinicService;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthoritiesService authoritiesService;

	@MockBean
	private ReservaService reservaService;
	
	@MockBean
	private AdopcionService adopcionService;

	@Autowired
	private MockMvc mockMvc;

	private Owner george;

	@BeforeEach
	void setup() {
		final User user = new User();
		user.setUsername("george");
		user.setPassword("123");
		user.setEnabled(true);

		final Authorities auth = new Authorities();
		auth.setAuthority("owner");
		auth.setId(1);
		auth.setUser(user);

		this.george = new Owner();
		this.george.setId(OwnerControllerTests.TEST_OWNER_ID);
		this.george.setFirstName("George");
		this.george.setLastName("Franklin");
		this.george.setAddress("110 W. Liberty St.");
		this.george.setCity("Madison");
		this.george.setTelephone("6085551023");
		this.george.setUser(user);
		BDDMockito.given(this.clinicService.findOwnerById(OwnerControllerTests.TEST_OWNER_ID)).willReturn(this.george);
		BDDMockito.given(this.reservaService.findReservasByOwner(TEST_OWNER_ID)).willReturn(new ArrayList<Reserva>());
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
		.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/new").param("firstName", "Joe").param("lastName", "Bloggs")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "01316761638"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("city", "London"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone"))
		.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/find")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
		.andExpect(MockMvcResultMatchers.view().name("owners/findOwners"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormSuccess() throws Exception {
		BDDMockito.given(this.clinicService.findOwnerByLastName("")).willReturn(Lists.newArrayList(this.george, new Owner()));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("owners/ownersList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormByLastName() throws Exception {
		BDDMockito.given(this.clinicService.findOwnerByLastName(this.george.getLastName())).willReturn(Lists.newArrayList(this.george));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "Franklin")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/" + OwnerControllerTests.TEST_OWNER_ID));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormNoOwnersFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "Unknown Surname")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "lastName"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
		.andExpect(MockMvcResultMatchers.view().name("owners/findOwners"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateOwnerForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/edit", OwnerControllerTests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
		.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("lastName", Matchers.is("Franklin"))))
		.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("firstName", Matchers.is("George"))))
		.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("address", Matchers.is("110 W. Liberty St."))))
		.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("city", Matchers.is("Madison"))))
		.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("telephone", Matchers.is("6085551023"))))
		.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", OwnerControllerTests.TEST_OWNER_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "01616291589"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", OwnerControllerTests.TEST_OWNER_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("city", "London"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone"))
		.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}", OwnerControllerTests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("lastName", Matchers.is("Franklin"))))
		.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("firstName", Matchers.is("George"))))
		.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("address", Matchers.is("110 W. Liberty St."))))
		.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("city", Matchers.is("Madison"))))
		.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("telephone", Matchers.is("6085551023"))))
		.andExpect(MockMvcResultMatchers.view().name("owners/ownerDetails"));
	}

	@WithMockUser(username = "spring", authorities = "admin")
	@Test
	void shouldDeleteOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/delete", OwnerControllerTests.TEST_OWNER_ID)).andExpect(
				MockMvcResultMatchers.view().name("owners/ownersList"));
	}
}
