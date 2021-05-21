package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name="solicitud_adopcion")
@Table(name= "solicitud_adopcion")
public class SolicitudAdopcion extends BaseEntity {
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="adopcion")
	private Adopcion adopcion;
	
	@NotEmpty
	@NotNull
	@Column(name = "solicitud")
	private String solicitud;
	
	@ManyToOne
	@JoinColumn(name = "nuevo_owner_id")
	private Owner nuevoOwner;
	
	@NotNull
	@Column(name = "fechaSolicitud")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaSolicitud;


	public Adopcion getAdopcion() {
		return adopcion;
	}

	public void setAdopcion(Adopcion adopcion) {
		this.adopcion = adopcion;
	}

	public String getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}

	public Owner getNuevoOwner() {
		return nuevoOwner;
	}

	public void setNuevoOwner(Owner nuevoOwner) {
		this.nuevoOwner = nuevoOwner;
	}

	public LocalDate getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(LocalDate fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	
}