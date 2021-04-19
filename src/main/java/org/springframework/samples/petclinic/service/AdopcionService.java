package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Adopcion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AdopcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AdopcionService {

	@Autowired
	private AdopcionRepository adopcionRepository;


	public Collection<Adopcion> findAll(){
		return adopcionRepository.findAll();
	}

	@Transactional (readOnly=true)
	public Optional<Adopcion> findById(int id) {
		return this.adopcionRepository.findById(id);
	}
	@Transactional (readOnly=true)
	public Collection<Adopcion> findAdopcionByIdOwnerId(int id) {
		return this.adopcionRepository.findAdopcionByPetOwnerId(id);
	}
	@Transactional (readOnly=true)
	public Adopcion findAdopcionByIdPetId(int id) {
		return this.adopcionRepository.findAdopcionByPetId(id);
	}
	//	@Transactional(readOnly = true)
	//	public List<Adopcion> findAdopcionesBetweenFechas(final LocalDate fechaIni, final LocalDate fechaFin) {
	//		return this.adopcionRepository.findAdopcionByFechaIniBeforeAndFechaFinAfter(fechaIni, fechaIni);
	//	}

	@Transactional
	public void save(final Adopcion adopcion) throws Exception {
		final Pet pet = adopcion.getPet();
		final Owner ownerOriginal = adopcion.getPet().getOwner();
		if(adopcion.getSolicitudAdopcion()==null) {
			adopcionRepository.save(adopcion);
		}
		else {
			final Set<Owner> owners= adopcion.getSolicitudAdopcion().stream().map(x->x.getNuevoOwner()).collect(Collectors.toSet());
			final LocalDate fechaPuestaEnAdopcion = adopcion.getFechaPuestaEnAdopcion();
			final Set<LocalDate> fechasPropuestaAdopcion = adopcion.getSolicitudAdopcion().stream().map(x->x.getFechaSolicitud()).collect(Collectors.toSet());
			final LocalDate fechaResolucionAdopcion = adopcion.getFechaResolucionAdopcion();

			if(owners.contains(ownerOriginal)) {
				throw new Exception("No puedes adoptar a tu propia mascota. "+pet.getName()+"encontrará pronto un nuevo dueño!");
			}

			if(fechaPuestaEnAdopcion==null) {
				throw new Exception("La fecha de puesta en adopción no es válida");
			}		
			if(fechaResolucionAdopcion!=null && (!compruebaFechasPropuestaEsPosteriorAFechaPuesta(fechaPuestaEnAdopcion, fechasPropuestaAdopcion) || !compruebaFechaResolucionEsPosteriorAFechasPropuestas(fechaResolucionAdopcion, fechasPropuestaAdopcion))) {
				throw new Exception("Las fecha de resolución de la adopción no es válida");
			}
			if((fechasPropuestaAdopcion!=null || !fechasPropuestaAdopcion.isEmpty()) && !compruebaFechasPropuestaEsPosteriorAFechaPuesta(fechaPuestaEnAdopcion, fechasPropuestaAdopcion)) {
				throw new Exception("La fecha de propuesta de adopcion debe ser después de la fecha de puesta en adopción");
			}

			this.adopcionRepository.save(adopcion);
		}
	}
	public Boolean compruebaFechasPropuestaEsPosteriorAFechaPuesta(LocalDate f,Set<LocalDate> fechas) {
		Boolean r=true;
		for(LocalDate fecha:fechas) {
			r=r&&fecha.isAfter(f);
		}
		return r;

	}
	public Boolean compruebaFechaResolucionEsPosteriorAFechasPropuestas(LocalDate f,Set<LocalDate> fechas) {
		Boolean r=true;
		for(LocalDate fecha:fechas) {
			r=r&&fecha.isBefore(f);
		}
		return r;

	}
	@Transactional
	public void delete(final Adopcion adopcion) {
		this.adopcionRepository.delete(adopcion);
	}

	//	public Collection<Adopcion> findAdopcionesByPet(int id){
	//		return this.adopcionRepository.findAdopcionesByPet(id);
	//	}
}
