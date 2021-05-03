package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Adopcion;

public interface AdopcionRepository extends CrudRepository<Adopcion, Integer>{
	Collection<Adopcion> findAll() throws DataAccessException;
	
	void delete(Adopcion adopcion) throws DataAccessException;

	public Collection<Adopcion> findAdopcionByPetOwnerId(int id);
	
	public Adopcion findAdopcionByPetId(int id);
}
