package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Specialty;

public interface SpecialtyRepository extends CrudRepository<Specialty, Integer>{

}
