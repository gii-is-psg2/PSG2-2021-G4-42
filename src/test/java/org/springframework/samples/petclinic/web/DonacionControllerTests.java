package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Causa;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CausaService;
import org.springframework.samples.petclinic.service.DonacionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=DonacionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class DonacionControllerTests {
	
	private static final int TEST_CAUSA_ID=1;
	private static final int TEST_OWNER_ID=1;
	
	@Autowired
	private DonacionController donacionController;
	
	@MockBean
	private DonacionService donacionService;
	
	@MockBean
	private CausaService causaService;
	
	@MockBean
	private OwnerService ownerService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Causa causa;
	private Owner owner;
	private User user;
	
	@BeforeEach
	void setup() {
		causa=new Causa();
		causa.setId(TEST_CAUSA_ID);
		causa.setNombre("Causa prueba");
		causa.setOrganizacion("Organizacion");
		causa.setDescripcion("Descripcion");
		causa.setRecaudacionObjetivo(1000000000.);
		
		owner=new Owner();
		owner.setFirstName("Pepe");
		owner.setAddress("Address");
		owner.setCity("Sevilla");
		owner.setId(TEST_OWNER_ID);
		owner.setLastName("Ramírez");
		owner.setTelephone("987653547");
		user=new User();
		user.setUsername("spring");
		user.setPassword("Password");
		user.setEnabled(true);
		owner.setUser(user);
		
		given(this.causaService.findById(TEST_CAUSA_ID)).willReturn(Optional.of(causa));
		given(this.ownerService.findOwnerByUsername("spring")).willReturn(Optional.of(owner));
	}
	
	@WithMockUser(value="spring", authorities="owner")
	@Test
	void testInitNewDonacion() throws Exception{
		mockMvc.perform(get("/donacion/{id}/new", TEST_CAUSA_ID))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("donacion"))
		.andExpect(model().attributeExists("causa"))
		.andExpect(view().name("donaciones/CreateOrUpdateDonacionForm"));	
	}
	
	@WithMockUser(value="spring")
	@Test
	void testProccessNewDonacion() throws Exception{
		mockMvc.perform(post("/donacion/{id}/new", TEST_CAUSA_ID)
				.param("cantidadDonada", "1000")
				.with(csrf())
				.param("causa", String.valueOf(TEST_CAUSA_ID))
				.param("donante", String.valueOf(TEST_OWNER_ID))
				.param("fechaDonacion", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
		.andExpect(status().isOk());
	}
	

}
