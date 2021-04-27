package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Causa;

public interface CausaRepository extends Repository<Causa, Integer>{
	
	Collection<Causa> findAll() throws DataAccessException;

	Optional<Causa> findById(int id) throws DataAccessException;

	void save(Causa causa) throws DataAccessException;
}
