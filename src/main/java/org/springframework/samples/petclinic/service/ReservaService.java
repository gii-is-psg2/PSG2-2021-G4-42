package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Habitacion;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.service.exceptions.FechaSeleccionadaNoValidaException;
import org.springframework.samples.petclinic.service.exceptions.MascotaOHabitacionNoLibreException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository reservaRepository;
	
	
	public Collection<Reserva> findAll(){
		return this.reservaRepository.findAll();
	}
	
	@Transactional (readOnly=true)
	public Optional<Reserva> findById(final int id) {
		return this.reservaRepository.findById(id);
	}
	
	@Transactional
	public void save(final Reserva reserva) throws MascotaOHabitacionNoLibreException, FechaSeleccionadaNoValidaException {
		final Pet pet = reserva.getPet();
		final Habitacion habitacion = reserva.getHabitacion();
		final LocalDate fechaIni = reserva.getFechaIni();
		final LocalDate fechaFin = reserva.getFechaFin();
		if(pet.estaOcupada(fechaIni, fechaFin) || habitacion.estaOcupada(fechaIni, fechaFin)) {
			throw new MascotaOHabitacionNoLibreException();
		}
		if(fechaFin.isBefore(fechaIni)||fechaIni.isBefore(LocalDate.now())||fechaFin.isBefore(LocalDate.now())) {
			throw new FechaSeleccionadaNoValidaException();
		}
		this.reservaRepository.save(reserva);
	}

	@Transactional
	public void delete(final Reserva reserva) {
		this.reservaRepository.delete(reserva);
	}
	
	@Transactional(readOnly=true)
	public Collection<Reserva> findReservasByOwner(final int id){
		return this.reservaRepository.findReservasByOwner(id);
	}
	
	@Transactional(readOnly=true)
	public Reserva findByFechaIni(LocalDate fechaIni) {
		return this.reservaRepository.findReservaByFechaIni(fechaIni);
	}
}
