package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.SpecialtyService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers=VetController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class VetControllerTests {

	private static final int TEST_VET_ID=2;
	
	@Autowired
	private VetController vetController;

	@MockBean
	private VetService clinicService;

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private SpecialtyService specialtyService;
	
	private Vet helen;
	private Vet james;
	private Specialty radiology;

	@BeforeEach
	void setup() {

		james=new Vet();
		helen=new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(1);
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setId(TEST_VET_ID);
		radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		helen.addSpecialty(radiology);
		given(this.clinicService.findVets()).willReturn(Lists.newArrayList(james, helen));
		given(this.clinicService.findById(TEST_VET_ID)).willReturn(Optional.of(helen));
	}
        
    @WithMockUser(value = "spring")
		@Test
	void testShowVetListHtml() throws Exception {
		mockMvc.perform(get("/vets")).andExpect(status().isOk()).andExpect(model().attributeExists("vets"))
				.andExpect(view().name("vets/vetList"));
	}	

	@WithMockUser(value = "spring")
        @Test
	void testShowVetListXml() throws Exception {
		mockMvc.perform(get("/vets.xml").accept(MediaType.APPLICATION_XML)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
				.andExpect(content().node(hasXPath("/vets/vetList[id=1]/id")));
	}
	
	@WithMockUser(value="spring")
	@Test
	void testinitCreationForm() throws Exception{
		mockMvc.perform(get("/vets/new")).andExpect(status().isOk())
		.andExpect(model().attributeExists("vet")).andExpect(view().name("vets/createOrUpdateVetForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationForm() throws Exception {
		mockMvc.perform(post("/vets/new").param("firstName", "Pepe").param("lastName", "Hidalgo")
					.with(csrf())
					.param("specialties", "surgery, radiology"))
					.andExpect(status().isOk());
	}
	
    @WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/vets/{id}/edit", TEST_VET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("vet"))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Leary"))))
				.andExpect(model().attribute("vet", hasProperty("firstName", is("Helen"))))
				.andExpect(model().attribute("vet", hasProperty("specialties", is(List.of(radiology)))))
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}
    
    @WithMockUser(value = "spring")
   	@Test
   	void testProcessUpdateForm() throws Exception {
   		mockMvc.perform(post("/vets/{id}/edit", TEST_VET_ID)
   							.with(csrf())
   							.param("firstName", "Joe")
   							.param("lastName", "Bloggs")
   							.param("specialties", "surgery, radiology"))
   				.andExpect(status().isOk())
   				.andExpect(view().name("vets/vetList"));
   	}


}
