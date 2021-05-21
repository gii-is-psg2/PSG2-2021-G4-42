package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.samples.petclinic.model.SolicitudAdopcion;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SolicitudAdopcionValidator implements Validator{
	private static final String REQUIRED = "requerido";
	private static final String NULL = "no debe ser nulo";
	private static final String FECHA_NO_VALIDA = "la fecha no es válida, debe seguir el patrón dd/MM/yyyy";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return SolicitudAdopcion.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		SolicitudAdopcion sa = (SolicitudAdopcion) obj;
		
		// Adopcion validation
		if(sa.getAdopcion() == null) {
			errors.rejectValue("adopcion", REQUIRED, NULL);
		}
		
		// Solicitud validation
		if(!StringUtils.hasLength(sa.getSolicitud()) || sa.getSolicitud() == null) {
			errors.rejectValue("solicitud", REQUIRED, "la solicitud no debe estar vacía");
		}
		
		// FechaSolicitud validation
		if(sa.getFechaSolicitud() == null) {
			errors.rejectValue("fechaSolicitud", REQUIRED, NULL);
		}
		
		try {
			LocalDate.parse(sa.getFechaSolicitud().toString(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (DateTimeParseException e) {
			errors.rejectValue("fechaSolicitud", REQUIRED, FECHA_NO_VALIDA);
		}
	}

}
