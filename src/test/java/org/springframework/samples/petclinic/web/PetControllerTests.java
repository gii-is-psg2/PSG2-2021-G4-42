package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
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
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */
@WebMvcTest(value = PetController.class,
		includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class PetControllerTests {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	@Autowired
	private PetController petController;


	@MockBean
	private PetService petService;
        
        @MockBean
	private OwnerService ownerService;

	@Autowired
	private MockMvc mockMvc;

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

		final Owner owner = new Owner();
		owner.setId(PetControllerTests.TEST_OWNER_ID);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		owner.setUser(user);
		
		final PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");
		
		final Pet pet = new Pet();
		pet.setId(PetControllerTests.TEST_PET_ID);
		
		owner.addPet(pet);
		
		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(cat));
		BDDMockito.given(this.ownerService.findOwnerById(PetControllerTests.TEST_OWNER_ID)).willReturn(owner);
		BDDMockito.given(this.petService.findPetById(PetControllerTests.TEST_PET_ID)).willReturn(pet);
	}

	@WithMockUser(value = "spring")
        @Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/new", PetControllerTests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm")).andExpect(MockMvcResultMatchers.model().attributeExists("pet"));
	}

	@WithMockUser(value = "spring")
        @Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", PetControllerTests.TEST_OWNER_ID)
							.with(SecurityMockMvcRequestPostProcessors.csrf())
							.param("name", "Betty")
							.param("type", "hamster")
							.param("birthDate", "2015/02/12"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", PetControllerTests.TEST_OWNER_ID, PetControllerTests.TEST_PET_ID)
							.with(SecurityMockMvcRequestPostProcessors.csrf())
							.param("name", "Betty")
							.param("birthDate", "2015/02/12"))
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("owner"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

    @WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/edit", PetControllerTests.TEST_OWNER_ID, PetControllerTests.TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("pet"))
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", PetControllerTests.TEST_OWNER_ID, PetControllerTests.TEST_PET_ID)
							.with(SecurityMockMvcRequestPostProcessors.csrf())
							.param("name", "Betty")
							.param("type", "hamster")
							.param("birthDate", "2015/02/12"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", PetControllerTests.TEST_OWNER_ID, PetControllerTests.TEST_PET_ID)
							.with(SecurityMockMvcRequestPostProcessors.csrf())
							.param("name", "Betty")
							.param("birthDate", "2015/02/12"))
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("owner"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}
    
    @WithMockUser(username = "spring", authorities = "admin")
    @Test
    void shouldDeletePet() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/delete", PetControllerTests.TEST_OWNER_ID, PetControllerTests.TEST_PET_ID)).andExpect(
    		MockMvcResultMatchers.view().name("redirect:/owners/" + String.valueOf(PetControllerTests.TEST_OWNER_ID)));
    }

}
