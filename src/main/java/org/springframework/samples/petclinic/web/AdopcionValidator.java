package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.samples.petclinic.model.Adopcion;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AdopcionValidator implements Validator{
	private static final String REQUIRED = "requerido";
	private static final String NULL = "no debe ser nulo";
	private static final String FECHA_NO_VALIDA = "la fecha no es válida, debe seguir el patrón dd/MM/yyyy";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Adopcion.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Adopcion adop = (Adopcion) obj;
		
		// Pet validation
		if(adop.getPet() == null) {
			errors.rejectValue("pet", REQUIRED, NULL);
		}
		
		// FechaPuestaEnAdopcion validation
		if(adop.getFechaPuestaEnAdopcion() == null) {
			errors.rejectValue("fechaPuestaEnAdopcion", REQUIRED, NULL);
		}
		
		try {
			LocalDate.parse(adop.getFechaPuestaEnAdopcion().toString(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (DateTimeParseException e) {
			errors.rejectValue("fechaPuestaEnAdopcion", REQUIRED, FECHA_NO_VALIDA);
		}
		
		// FechaResolucionAdopcion validation
		try {
			LocalDate.parse(adop.getFechaResolucionAdopcion().toString(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (DateTimeParseException e) {
			errors.rejectValue("fechaResolucionAdopcion", FECHA_NO_VALIDA);
		}
		
	}

}
