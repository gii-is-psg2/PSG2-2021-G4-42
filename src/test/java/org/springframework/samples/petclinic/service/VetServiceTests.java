/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class VetServiceTests {

	@Autowired
	protected VetService vetService;
	
	@Autowired
	protected SpecialtyService specialtyService;
	
	public Vet vet;
	
	@BeforeEach
	void insertarVet() throws DataAccessException{
		Vet vet = new Vet();
		
		vet.setFirstName("Nombre de prueba PSG2");
		vet.setLastName("Apellidos de prueba PSG2");
		vet.setSpecialties(specialtyService.findAll());
		
		vetService.save(vet);
		this.vet=vet;
	}

	@Test
	void shouldFindVets() {
		final Collection<Vet> vets = this.vetService.findVets();

		final Vet vet = EntityUtils.getById(vets, Vet.class, 3);
		Assertions.assertThat(vet.getLastName()).isEqualTo("Douglas");
		Assertions.assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		//Assertions.assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("cirugía");
		//Assertions.assertThat(vet.getSpecialties().get(1).getName()).isEqualTo("odontología");
	}
	
	@Test
	@Transactional
	void shouldAddNewVet() throws DataAccessException{
		assertEquals(vet, vetService.findByFirstName("Nombre de prueba PSG2").get());
	}
	
	@Test
	@Transactional
	void shouldUpdateVet() throws DataAccessException{
		Vet vet1 = this.vetService.findByFirstName("Nombre de prueba PSG2").get();
		vet1.setFirstName("Nombre de prueba PSG2 modificado");
		vet1.setLastName("Apellidos de prueba PSG2 modficado");
		List<Specialty> specialties = specialtyService.findAll().stream().collect(Collectors.toList());
		Specialty s = specialties.get(1);
		List<Specialty> listaEspecialidad = new ArrayList<>();
		listaEspecialidad.add(s);
		vet1.setSpecialties(listaEspecialidad);
		
		vetService.save(vet1);
		
		assertFalse(vetService.findByFirstName("Nombre de prueba PSG2").isPresent());
		assertTrue(vetService.findByFirstName("Nombre de prueba PSG2 modificado").isPresent());
		
		Vet vet2 = this.vetService.findByFirstName("Nombre de prueba PSG2 modificado").get();
		Assertions.assertThat(vet2.getLastName()).isEqualTo("Apellidos de prueba PSG2 modficado");
		Assertions.assertThat(vet2.getSpecialties()).isEqualTo(listaEspecialidad);
	}
	
	@Test
	@Transactional
	void shouldDeleteVet() {
		final Optional<Vet> vet = this.vetService.findById(1);
		
		this.vetService.delete(vet.get());
		
		Assert.assertFalse(this.vetService.findById(1).isPresent());
	}


}
