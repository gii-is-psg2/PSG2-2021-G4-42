package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Donacion;

public interface DonacionRepository extends Repository<Donacion, Integer>{
	
	void save(Donacion donacion) throws DataAccessException;
	
	@Query(value="SELECT * FROM DONACIONES WHERE ID_CAUSA = :id", nativeQuery = true)
	public Collection<Donacion> findDonacionesByCausa(int id);

}
