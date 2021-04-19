package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.SolicitudAdopcion;

public interface SolicitudAdopcionRepository extends  CrudRepository<SolicitudAdopcion, Integer>{
	
	Collection<SolicitudAdopcion> findAll() throws DataAccessException;
	

//	@Query(value="SELECT S.ID, SOLICITUD,FECHA_SOLICITUD, NUEVO_OWNER_ID FROM SOLICITUD_ADOPCION AS A LEFT JOIN PETS AS P WHERE PET_ID = :id", nativeQuery = true)
//	public Collection<SolicitudAdopcion> findSolicitudAdopcionById(int id);

//	@Query("SELECT s FROM solicitud_adopcion s where s.adopcion.pet.id = ?1")
	Set<SolicitudAdopcion> findSolicitudAdopcionByAdopcionPetId(int petId);
	
//	
//	@Query("SELECT COUNT(s) FROM Solicitud_Adopcion")
//	Integer numeroDeSolicitudes() throws DataAccessException;
	

}
