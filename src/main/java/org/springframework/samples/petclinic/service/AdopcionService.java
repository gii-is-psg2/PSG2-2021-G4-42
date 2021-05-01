package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Adopcion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AdopcionRepository;
import org.springframework.samples.petclinic.service.exceptions.CannotAdoptYourOwnPetException;
import org.springframework.samples.petclinic.service.exceptions.FechaPropuestaDebeSerPosteriorFechaAdopcionException;
import org.springframework.samples.petclinic.service.exceptions.FechaPuestaEnAdopcionNoValidaException;
import org.springframework.samples.petclinic.service.exceptions.FechaResolucionAdopcionNoValidaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AdopcionService {

	@Autowired
	private AdopcionRepository adopcionRepository;


	public Collection<Adopcion> findAll(){
		return this.adopcionRepository.findAll();
	}

	@Transactional (readOnly=true)
	public Optional<Adopcion> findById(final int id) {
		return this.adopcionRepository.findById(id);
	}
	@Transactional (readOnly=true)
	public Collection<Adopcion> findAdopcionByIdOwnerId(final int id) {
		return this.adopcionRepository.findAdopcionByPetOwnerId(id);
	}
	@Transactional (readOnly=true)
	public Adopcion findAdopcionByIdPetId(final int id) {
		return this.adopcionRepository.findAdopcionByPetId(id);
	}

	@Transactional
	public void save(final Adopcion adopcion)
			throws CannotAdoptYourOwnPetException, FechaPuestaEnAdopcionNoValidaException,
			FechaResolucionAdopcionNoValidaException, FechaPropuestaDebeSerPosteriorFechaAdopcionException {
		final Pet pet = adopcion.getPet();
		final Owner ownerOriginal = pet.getOwner();
		if(adopcion.getSolicitudAdopcion()==null) {
			this.adopcionRepository.save(adopcion);
		}
		else {
			final Set<Owner> owners= adopcion.getSolicitudAdopcion().stream().map(x->x.getNuevoOwner()).collect(Collectors.toSet());
			final LocalDate fechaPuestaEnAdopcion = adopcion.getFechaPuestaEnAdopcion();
			final Set<LocalDate> fechasPropuestaAdopcion = adopcion.getSolicitudAdopcion().stream().map(x->x.getFechaSolicitud()).collect(Collectors.toSet());
			final LocalDate fechaResolucionAdopcion = adopcion.getFechaResolucionAdopcion();

			if(owners.contains(ownerOriginal)) {
				throw new CannotAdoptYourOwnPetException();
			}

			if(fechaPuestaEnAdopcion==null) {
				throw new FechaPuestaEnAdopcionNoValidaException();
			}		
			if(!this.compruebaFechasPropuestaEsPosteriorAFechaPuesta(fechaPuestaEnAdopcion, fechasPropuestaAdopcion) || !this.compruebaFechaResolucionEsPosteriorAFechasPropuestas(fechaResolucionAdopcion, fechasPropuestaAdopcion)) {
				throw new FechaResolucionAdopcionNoValidaException();
			}
			if(!fechasPropuestaAdopcion.isEmpty() && !this.compruebaFechasPropuestaEsPosteriorAFechaPuesta(fechaPuestaEnAdopcion, fechasPropuestaAdopcion)) {
				throw new FechaPropuestaDebeSerPosteriorFechaAdopcionException();
			}

			this.adopcionRepository.save(adopcion);
		}
	}
	public boolean compruebaFechasPropuestaEsPosteriorAFechaPuesta(final LocalDate f,final Set<LocalDate> fechas) {
		boolean r=true;
		for(final LocalDate fecha:fechas) {
			r=r&&fecha.isAfter(f);
		}
		return r;

	}
	public boolean compruebaFechaResolucionEsPosteriorAFechasPropuestas(final LocalDate f,final Set<LocalDate> fechas) {
		boolean r=true;
		for(final LocalDate fecha:fechas) {
			r=r&&fecha.isBefore(f);
		}
		return r;

	}
	@Transactional
	public void delete(final Adopcion adopcion) {
		this.adopcionRepository.delete(adopcion);
	}
}
