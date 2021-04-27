package org.springframework.samples.petclinic.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Habitacion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.HabitacionService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservaControllerTests {
	
	private static final int TEST_RESERVA_ID = 0;
	
	@Autowired
    private WebApplicationContext context; 
	
	private MockMvc mockMvc;
	
	@MockBean
	PetService petService;
	
	@MockBean
	ReservaService reservaService;
	
	@MockBean
	HabitacionService habitacionService;
	
	@BeforeEach
	void setup() throws InterruptedException, IOException {
		this.mockMvc = MockMvcBuilders
		          .webAppContextSetup(this.context)
		          .apply(SecurityMockMvcConfigurers.springSecurity())
		          .build();
		
		final Habitacion habitacion = new Habitacion();
		final Pet pet = new Pet();
		pet.setName("Sly");
		final Owner owner = new Owner();
		owner.setId(1);
		
		final User u = new User();
		u.setUsername("spring");
		owner.setUser(u);
		owner.addPet(pet);
	//	try {
	//		petService.savePet(pet);
	//	} catch (DataAccessException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	} catch (DuplicatedPetNameException e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
		habitacion.setNumero(1);
	//	habitacionService.save(habitacion);
		
		final Reserva reserva = new Reserva();
		reserva.setId(ReservaControllerTests.TEST_RESERVA_ID);
		reserva.setFechaIni(LocalDate.now());
		reserva.setFechaFin(LocalDate.now());
		reserva.setHabitacion(habitacion);
		reserva.setPet(pet);
		
		BDDMockito.given(this.reservaService.findAll()).willReturn(new ArrayList<Reserva>());
		BDDMockito.given(this.petService.findPetsByOwner(ArgumentMatchers.anyString())).willReturn(new ArrayList<Pet>());
		BDDMockito.given(this.habitacionService.findAll()).willReturn(new ArrayList<Habitacion>());
		BDDMockito.given(this.habitacionService.findHabitacionByNumero(1)).willReturn(Optional.of(habitacion));
		BDDMockito.given(this.petService.findPetById(1)).willReturn(pet);
		BDDMockito.given(this.reservaService.findById(ReservaControllerTests.TEST_RESERVA_ID)).willReturn(Optional.of(reserva));
	
	
	
	}
	
	@WithMockUser(value="spring", authorities = "owner")
	@Test
	void testComprobarUrls() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva")).andExpect(MockMvcResultMatchers.status().isOk());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva/new")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@WithMockUser(value="spring")
	@Test
	void noAccessTo() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva/new")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	@WithMockUser(value="spring", authorities = "owner")
	@Test
	void testGetReservas() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva"))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/reserva/reservaList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("reservas"));
	}
	
	@WithMockUser(value="spring")
	@Test
	void testNotGetReservas() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva"))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	@WithMockUser(value="spring", authorities = "owner")
	@Test
	void testInitCreationReserva1() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.post("/reserva/new/fechas")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("fechaIni", LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
			.param("fechaFin", LocalDate.now().plusYears(2).plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("reserva/createOrUpdateReservaForm"));
	}
	
	
	@WithMockUser(value="spring")
	@Test
	void testNotInitCreationReserva() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.post("/reserva/new/fechas")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("fechaIni", LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
			.param("fechaFin", LocalDate.now().plusYears(2).plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	@WithMockUser(value="spring", authorities = "owner")
	@Test
	void testInitCreationRerserva2() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/reserva/new").
			with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("fechaIni", LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
			.param("FechaFin", LocalDate.now().plusYears(2).plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
			.param("pet", "1_Sly - cat")
			.param("habitacion", "Habitación nº 1"))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("welcome"));
	}
	
	@WithMockUser(value="spring")
	@Test
	void testNotInitCreationRerserva2() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.post("/reserva/new").
			with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("fechaIni", LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
			.param("FechaFin", LocalDate.now().plusYears(2).plusDays(4).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
			.param("pet", "1_Sly - cat")
			.param("habitacion", "Habitación nº 1"))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
    @WithMockUser(username = "spring", authorities = "admin")
    @Test
    void shouldDeleteReserva() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva/delete/{reservaId}", ReservaControllerTests.TEST_RESERVA_ID)).andExpect(
    		MockMvcResultMatchers.view().name("redirect:/owners/1"));
    }
	
	

}