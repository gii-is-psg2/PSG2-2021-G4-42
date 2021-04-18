package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Causa;
import org.springframework.samples.petclinic.model.Donacion;
import org.springframework.samples.petclinic.repository.CausaRepository;
import org.springframework.samples.petclinic.repository.DonacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CausaService {

	@Autowired
	private CausaRepository causaRepository;
	
	@Autowired
	private DonacionRepository donacionRepository;
	
	public Collection<Causa> findAll(){
		return causaRepository.findAll();
	}
	
	@Transactional
	public Optional<Causa> findById(int id){
		return this.causaRepository.findById(id);
	}
	
	@Transactional
	public void save(@Valid final Causa causa) throws Exception {
		this.causaRepository.save(causa);
	}
	
	@Transactional
	public Collection<Donacion> findDonacionesByCausa(Integer id) {
		return this.donacionRepository.findDonacionesByCausa(id);
	}
}
