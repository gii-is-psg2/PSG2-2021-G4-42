package org.springframework.samples.petclinic.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.SolicitudAdopcion;
import org.springframework.samples.petclinic.repository.SolicitudAdopcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

	@Service
	public class SolicitudAdopcionService {

		private final SolicitudAdopcionRepository solicitudAdopcionRepository;	

		@Autowired
		public SolicitudAdopcionService(final SolicitudAdopcionRepository solicitudAdopcionRepository) {
			this.solicitudAdopcionRepository = solicitudAdopcionRepository;
		}	

		@Transactional(readOnly = true)
		public Optional<SolicitudAdopcion> findSolicitudById(final int id) throws DataAccessException {
			return this.solicitudAdopcionRepository.findById(id);
		}


		@Transactional
		public void saveSolicitud(final SolicitudAdopcion s) throws DataAccessException {
			this.solicitudAdopcionRepository.save(s);		
		}			
		
		@Transactional
		public void deleteSolicitud(final SolicitudAdopcion s) throws DataAccessException{
			this.solicitudAdopcionRepository.delete(s);
		}

		public Set<SolicitudAdopcion> findSolicitudAdopcionByPetId(final int petId) {						
			return this.solicitudAdopcionRepository.findSolicitudAdopcionByAdopcionPetId(petId);
		}

	}
