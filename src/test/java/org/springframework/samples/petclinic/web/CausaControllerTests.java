package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.service.CausaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers=CausaController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class CausaControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	CausaService causaService;
	
	@WithMockUser(value = "spring")
    @Test
	void testCausas() throws Exception {
	
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causa")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("causas/causaList"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testCreateNewCausa() throws Exception {
	
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causa/new")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("causas/createOrUpdateCausaForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testPostNewCausa() throws Exception {
	
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causa/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("nombre", "Joe")
				.param("organizacion", "organi")
				.param("descripcion", "descripcion")
				.param("recaudacionObjetivo", "10000."))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/causa/"));
	}
}
