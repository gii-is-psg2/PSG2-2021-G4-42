/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adopcion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.AdopcionRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PetService {

	private final PetRepository petRepository;

	private final VisitRepository visitRepository;

	private final AdopcionRepository adopcionRepository;

	private final OwnerService ownserService;


	@Autowired
	public PetService(final PetRepository petRepository,
			final VisitRepository visitRepository, final OwnerService ownerService, final AdopcionRepository adopcionRepository) {
		this.petRepository = petRepository;
		this.visitRepository = visitRepository;
		this.ownserService = ownerService;
		this.adopcionRepository = adopcionRepository;

	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return this.petRepository.findPetTypes();
	}

	@Transactional
	public void saveVisit(final Visit visit) throws DataAccessException {
		this.visitRepository.save(visit);
	}

	@Transactional(readOnly = true)
	public Pet findPetById(final int id) throws DataAccessException {
		return this.petRepository.findById(id);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void savePet(final Pet pet) throws DataAccessException, DuplicatedPetNameException {
		final Pet otherPet=pet.getOwner().getPetwithIdDifferent(pet.getName(), pet.getId());
		if (StringUtils.hasLength(pet.getName()) &&  (otherPet!= null && otherPet.getId().equals(pet.getId()))) {            	
			throw new DuplicatedPetNameException();
		}else
			this.petRepository.save(pet);                
	}


	public Collection<Visit> findVisitsByPetId(final int petId) {
		return this.visitRepository.findByPetId(petId);
	}

	public List<Pet> findPetsByOwner(final String username){
		final Optional<Owner> owner = this.ownserService.findOwnerByUsername(username);
		if(owner.isPresent()) {
			return this.petRepository.findPetByOwner(owner.get());
		}else {
			return new ArrayList<>();
		}
	}

	public Optional<Pet> findPetByName(final String name) {
		return this.petRepository.findPetByName(name);
	}

	@Transactional
	public void delete(final Pet p) throws DataAccessException{
		final Adopcion a=this.adopcionRepository.findAdopcionByPetId(p.getId());
		if(a!=null) {
			this.adopcionRepository.delete(a);
		}
		this.petRepository.delete(p);
	}


}
