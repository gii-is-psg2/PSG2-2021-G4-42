package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.SolicitudAdopcion;

public interface SolicitudAdopcionRepository extends  CrudRepository<SolicitudAdopcion, Integer>{
	
	Collection<SolicitudAdopcion> findAll() throws DataAccessException;
	
	Set<SolicitudAdopcion> findSolicitudAdopcionByAdopcionPetId(int petId);
}
