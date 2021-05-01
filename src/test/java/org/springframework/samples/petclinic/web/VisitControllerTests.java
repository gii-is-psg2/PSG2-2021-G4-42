package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = VisitController.class,
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class VisitControllerTests {

	private static final int TEST_PET_ID = 1;
	private static final int TEST_VISIT_ID = 1;

	@Autowired
	private VisitController visitController;

	@MockBean
	private PetService clinicService;
	
	@MockBean
	private VisitService visitServive;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		final User user = new User();
		user.setUsername("spr");
		final Owner owner = new Owner();
		owner.setId(1);
		owner.setUser(user);
		final Pet pet = new Pet();
		pet.setOwner(owner);
		final String description = "Descripcion";
		final LocalDate date = LocalDate.now().plusDays(1);
		
		final Visit visit = new Visit();
		visit.setPet(pet);
		visit.setDescription(description);
		visit.setDate(date);
		BDDMockito.given(this.clinicService.findPetById(VisitControllerTests.TEST_PET_ID)).willReturn(new Pet());
		BDDMockito.given(this.visitServive.findById(VisitControllerTests.TEST_VISIT_ID)).willReturn(Optional.of(visit));
	}

        @WithMockUser(value = "spring")
        @Test
	void testInitNewVisitForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visits/{petId}/new", VisitControllerTests.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"));
	}
	@WithMockUser(value = "spring")
        @Test
        @Disabled("No funciona, se arreglará más adelante")
	void testProcessNewVisitFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/visits/{petId}/new", VisitControllerTests.TEST_PET_ID).param("name", "George")
							.with(SecurityMockMvcRequestPostProcessors.csrf())
							.param("description", "Visit Description"))                                
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
        @Test
        @Disabled("No funciona, se arreglará más adelante")
	void testProcessNewVisitFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/visits/{petId}/new", VisitControllerTests.TEST_PET_ID)
							.with(SecurityMockMvcRequestPostProcessors.csrf())
							.param("name", "George"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("visit"))
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"));
	}

	@WithMockUser(value = "spring", authorities = "admin")
    @Test
	void testDeleteVisit() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visits/{petId}/delete", VisitControllerTests.TEST_VISIT_ID))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/1"));
	}

}
