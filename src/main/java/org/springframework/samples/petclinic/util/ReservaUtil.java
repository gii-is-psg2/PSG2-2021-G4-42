package org.springframework.samples.petclinic.util;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.samples.petclinic.model.Reserva;

public class ReservaUtil {
	
	private ReservaUtil() {
	    throw new IllegalStateException("Utility class");
	}

	public static Boolean estaOcupada(final LocalDate fechaIni, final LocalDate fechaFin, final Set<Reserva> reservas) {
		Boolean res = false;
		for(final Reserva reserva : reservas) {
			res = res || (reserva.getFechaIni().isAfter(fechaIni) || reserva.getFechaIni().equals(fechaIni)) 
				&& (reserva.getFechaIni().isBefore(fechaFin) || reserva.getFechaIni().equals(fechaFin))
				|| (reserva.getFechaFin().isAfter(fechaIni)  || reserva.getFechaFin().equals(fechaIni))
				&& (reserva.getFechaFin().isBefore(fechaFin) || reserva.getFechaFin().equals(fechaFin))
				|| (reserva.getFechaIni().isBefore(fechaIni) || reserva.getFechaIni().equals(fechaIni)) 
				&& (reserva.getFechaFin().isAfter(fechaFin)  || reserva.getFechaFin().equals(fechaFin));
		}
		return res;
	}
}
