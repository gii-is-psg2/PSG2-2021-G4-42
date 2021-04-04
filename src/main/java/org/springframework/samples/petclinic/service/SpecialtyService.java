package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

@Service
public class SpecialtyService {
	
	@Autowired
	public SpecialtyRepository specialtyRepo;
	
	public List<Specialty> findAll() {
		return specialtyRepo.findAll();
		
	}

}
