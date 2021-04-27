package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Adopcion;

public interface AdopcionRepository extends CrudRepository<Adopcion, Integer>{
	Collection<Adopcion> findAll() throws DataAccessException;

//List<Adopcion> findReservaByFechaIniBeforeAndFechaFinAfter(LocalDate fechaIni, LocalDate fechaFin) throws DataAccessException;
	
	void delete(Adopcion adopcion) throws DataAccessException;
//	@Query(value="SELECT R.ID, FECHA_FIN, FECHA_INI, HABITACION_ID, PET_ID FROM ADOPCION AS R LEFT JOIN PETS AS P WHERE PET_ID = P.ID AND P.OWNER_ID = :id", nativeQuery = true)
//	public Collection<Adopcion> findAdopcionByPet(int id);
	
//	@Query(value="SELECT A.ID, FECHA_PUESTA_EN_ADOPCION,FECHA_RESOLUCION_ADOPCION, PET_ID,solicitud_id FROM ADOPCION AS A LEFT JOIN PETS AS P WHERE PET_ID = P.ID AND P.OWNER_ID = :id", nativeQuery = true)
	public Collection<Adopcion> findAdopcionByPetOwnerId(int id);
	
//	@Query(value="SELECT ID, FECHA_PUESTA_EN_ADOPCION,FECHA_RESOLUCION_ADOPCION, PET_ID,solicitud_id FROM ADOPCION  WHERE PET_ID = :id", nativeQuery = true)
	public Adopcion findAdopcionByPetId(int id);
}
