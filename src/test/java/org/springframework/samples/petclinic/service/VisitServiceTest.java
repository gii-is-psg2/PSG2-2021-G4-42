package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class VisitServiceTest {

	@Autowired
	private VisitService visitService;
	
	private static final Integer VISIT_ID = 1;
	
	@Test
	void shouldDeleteVisit() {
		final Optional<Visit> visit = this.visitService.findById(VisitServiceTest.VISIT_ID);
		
		Assert.assertTrue(visit.isPresent());
		
		this.visitService.deleteVisit(visit.get());
		
		Assert.assertFalse(this.visitService.findById(VisitServiceTest.VISIT_ID).isPresent());
	}
}
