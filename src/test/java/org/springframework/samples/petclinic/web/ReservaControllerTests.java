package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Habitacion;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.service.HabitacionService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservaControllerTests {
	
	private static final int TEST_RESERVA_ID = 0;
	
	@Autowired
    private WebApplicationContext context; 
 
	@MockBean
	PetFormatter petformatter;
	
	@MockBean
	HabitacionFormatter habitacionformatter;
	
	private MockMvc mockMvc;
	
	@MockBean
	PetService petService;
	
	@MockBean
	ReservaService reservaService;
	
	@MockBean
	HabitacionService habitacionService;
	
	@BeforeEach
	void setup() throws InterruptedException, IOException {
	mockMvc = MockMvcBuilders
	          .webAppContextSetup(context)
	          .apply(SecurityMockMvcConfigurers.springSecurity())
	          .build();
	
	Habitacion habitacion = new Habitacion();
	Pet pet = new Pet();
	pet.setName("Sly");
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
	
	Reserva reserva = new Reserva();
	reserva.setId(TEST_RESERVA_ID);
	reserva.setFechaIni(LocalDate.now());
	reserva.setFechaFin(LocalDate.now());
	reserva.setHabitacion(habitacion);
	reserva.setPet(pet);
	
	given(this.reservaService.findAll()).willReturn(new ArrayList<Reserva>());
	given(this.petService.findPetsByOwner(Mockito.anyString())).willReturn(new ArrayList<Pet>());
	given(this.habitacionService.findAll()).willReturn(new ArrayList<Habitacion>());
	given(this.habitacionService.findHabitacionByNumero(1)).willReturn(Optional.of(habitacion));
	given(this.petService.findPetByName("Sly")).willReturn(Optional.of(pet));
	
	
	
	}
	
	@WithMockUser(value="spring", authorities = "owner")
	@Test
	void testComprobarUrls() throws Exception {
		mockMvc.perform(get("/reserva")).andExpect(status().isOk());
		mockMvc.perform(get("/reserva/new")).andExpect(status().isOk());
	}
	
	@WithMockUser(value="spring")
	@Test
	void noAccessTo() throws Exception {
		mockMvc.perform(get("/reserva")).andExpect(status().is4xxClientError());
		mockMvc.perform(get("/reserva/new")).andExpect(status().is4xxClientError());
	}
	
	@WithMockUser(value="spring", authorities = "owner")
	@Test
	void testGetReservas() throws Exception{
		mockMvc.perform(get("/reserva"))
		.andExpect(status().isOk()).andExpect(view().name("/reserva/reservaList"))
		.andExpect(model().attributeExists("reservas"));
	}
	
	@WithMockUser(value="spring")
	@Test
	void testNotGetReservas() throws Exception{
		mockMvc.perform(get("/reserva"))
		.andExpect(status().is4xxClientError());
	}
	
	@WithMockUser(value="spring", authorities = "owner")
	@Test
	void testInitCreationReserva1() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/reserva/new/fechas")
			.with(csrf())
			.param("fechaIni", LocalDate.of(2022, 10, 15).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
			.param("fechaFin", LocalDate.of(2022, 10, 30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
		.andExpect(status().isOk()).andExpect(view().name("reserva/createOrUpdateReservaForm"));
	}
	
	
	@WithMockUser(value="spring")
	@Test
	void testNotInitCreationReserva() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/reserva/new/fechas")
			.with(csrf())
			.param("fechaIni", LocalDate.of(2022, 10, 15).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
			.param("fechaFin", LocalDate.of(2022, 10, 30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
		.andExpect(status().is4xxClientError());
	}
	
//	@WithMockUser(value="spring", authorities = "owner")
//	@Test
//	void testInitCreationRerserva2() throws Exception{
//		mockMvc.perform(MockMvcRequestBuilders.post("/reserva/new").
//			with(csrf())
//			.param("fechaIni", LocalDate.of(2022, 10, 15).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
//			.param("FechaFin", LocalDate.of(2022, 10, 30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
//			.param("pet", "Sly - cat")
//			.param("habitacion", "Habitación nº 1"))
//		.andExpect(status().isOk()).andExpect(view().name("welcome"));
//	}
	
	@WithMockUser(value="spring")
	@Test
	void testNotInitCreationRerserva2() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/reserva/new").
			with(csrf())
			.param("fechaIni", LocalDate.of(2022, 10, 15).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
			.param("FechaFin", LocalDate.of(2022, 10, 30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
			.param("pet", "Sly - cat")
			.param("habitacion", "Habitación nº 1"))
		.andExpect(status().is4xxClientError());
	}
	
	

}
