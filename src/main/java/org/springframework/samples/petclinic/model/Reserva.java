package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name="reserva")
@Table(name= "reserva")
public class Reserva extends BaseEntity {
	
	@NotNull
	@Column(name = "fecha_ini")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate fechaIni;
	
	@NotNull
	@Column(name = "fecha_fin")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate fechaFin;

	
	public LocalDate getFechaIni() {
		return this.fechaIni;
	}

	
	public void setFechaIni(final LocalDate fechaIni) {
		this.fechaIni = fechaIni;
	}

	
	public LocalDate getFechaFin() {
		return this.fechaFin;
	}

	
	public void setFechaFin(final LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	
	
}
