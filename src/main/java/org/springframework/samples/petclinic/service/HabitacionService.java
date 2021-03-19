package org.springframework.samples.petclinic.service;


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
	public Habitacion findHabitacionByNumero(final Integer numero) {
		return this.habitacionRepository.findHabitacionByNumero(numero);
	}
}
