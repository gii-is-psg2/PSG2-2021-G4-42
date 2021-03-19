package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public void save(final Reserva reserva) {
		this.reservaRepository.save(reserva);
	}
	
	@Transactional
	public void delete(final Reserva reserva) {
		this.reservaRepository.delete(reserva);
	}
}
