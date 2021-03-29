package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

@Service
public class SpecialtyService {
	
	@Autowired
	public SpecialtyRepository specialtyRepo;
	
	public Iterable<Specialty> findAll() {
		return specialtyRepo.findAll();
		
	}

}
