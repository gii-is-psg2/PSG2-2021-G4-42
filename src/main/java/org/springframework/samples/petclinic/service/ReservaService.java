package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Habitacion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository reservaRepository;
	
	@Autowired
	private OwnerService ownerService;
	
	@Transactional(readOnly = true)
	public List<Reserva> findReservasBetweenFechas(final LocalDate fechaIni, final LocalDate fechaFin) {
		return this.reservaRepository.findReservaByFechaIniBeforeAndFechaFinAfter(fechaIni, fechaIni);
	}
	
	@Transactional
	public void save(final Reserva reserva) throws Exception {
		final Pet pet = reserva.getPet();
		final Habitacion habitacion = reserva.getHabitacion();
		final LocalDate fechaIni = reserva.getFechaIni();
		final LocalDate fechaFin = reserva.getFechaFin();
		if(pet.estaOcupada(fechaIni, fechaFin) || habitacion.estaOcupada(fechaIni, fechaFin)) {
			throw new Exception("La mascota o habitación deben de estar libre");
		}
		this.reservaRepository.save(reserva);
	}
	
	@Transactional
	public void delete(final Reserva reserva) {
		this.reservaRepository.delete(reserva);
	}
	
	@Transactional(readOnly = true)
	public List<Pet> findPetReservaBetweenFechasAndUsername(final LocalDate fechaIni, final LocalDate fechaFin, final String username){
		final Optional<Owner> owner = this.ownerService.findOwnerByUsername(username);
		if(!owner.isPresent()) {
			return new ArrayList<>();
		}
		return this.reservaRepository.findReservaPetByFechaIniAfterAndFechaFinBeforeAndPetOwner(fechaIni, fechaFin, owner.get());
	}
	
	@Transactional(readOnly = true)
	public Set<Habitacion> findHabitacionReservaBetweenFechas(final LocalDate fechaIni, final LocalDate fechaFin){
		return this.reservaRepository.findReservaHabitacionByFechaIniAfterAndFechaFinBefore(fechaIni, fechaFin);
	}
}
