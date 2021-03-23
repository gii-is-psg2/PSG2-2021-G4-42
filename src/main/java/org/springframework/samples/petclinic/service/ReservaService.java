package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Habitacion;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository reservaRepository;
	
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
}
