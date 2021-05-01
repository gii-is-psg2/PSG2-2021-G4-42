package org.springframework.samples.petclinic.service;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Donacion;
import org.springframework.samples.petclinic.repository.DonacionRepository;
import org.springframework.samples.petclinic.service.exceptions.DonacionNoValidaException;
import org.springframework.stereotype.Service;

@Service
public class DonacionService {

	@Autowired
	private DonacionRepository donacionRepository;
	
	@Transactional
	public void save(@Valid final Donacion donacion) throws DonacionNoValidaException{
		this.donacionRepository.save(donacion);
	}

	
	
	
}
