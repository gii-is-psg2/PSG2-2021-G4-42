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
package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Reserva;

/**
 * Spring Data JPA specialization of the {@link ReservaRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface ReservaRepository extends CrudRepository<Reserva, Integer> {
	
	@Override
	Collection<Reserva> findAll() throws DataAccessException;
	
	Reserva findReservaByFechaIni(LocalDate fechaIni);
	
	@Query(value="SELECT R.ID, FECHA_FIN, FECHA_INI, HABITACION_ID, PET_ID FROM RESERVA AS R LEFT JOIN PETS AS P WHERE PET_ID = P.ID AND P.OWNER_ID = :id", nativeQuery = true)
	public Collection<Reserva> findReservasByOwner(int id);
	
	
}
