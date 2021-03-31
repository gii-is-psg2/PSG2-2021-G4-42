package org.springframework.samples.petclinic.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Habitacion;
import org.springframework.samples.petclinic.repository.HabitacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HabitacionService {

	@Autowired
	private HabitacionRepository habitacionRepository;
	
	@Transactional
	public Optional<Habitacion> findHabitacionByNumero(final Integer numero) {
		return this.habitacionRepository.findHabitacionByNumero(numero);
	}
	
	@Transactional(readOnly = true)
	public Integer totalDeHabitaciones() {
		return this.habitacionRepository.totalDeHabitaciones();
	}
	
	@Transactional(readOnly = true)
	public List<Habitacion> findAll() {
		return this.habitacionRepository.findAll();
	}

	public void save(Habitacion habitacion) {
		this.habitacionRepository.save(habitacion);
		
	}
}
