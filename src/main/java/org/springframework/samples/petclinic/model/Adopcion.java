package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
//
@Entity(name="adopcion")
@Table(name= "adopcion")
public class Adopcion extends BaseEntity{
	
	@NotNull
	@Column(name = "fecha_puesta_en_adopcion")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate fechaPuestaEnAdopcion;
	
	@Column(name = "fecha_resolucion_adopcion")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate fechaResolucionAdopcion;
	
	@NotNull
	@JoinColumn(name = "pet_id", unique = true)
	@OneToOne
	private Pet pet;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy ="adopcion")
	private Set<SolicitudAdopcion> solicitudAdopcion;

	@Override
	public String toString() {
		return "Adopcion [fechaPuestaEnAdopcion=" + fechaPuestaEnAdopcion +", fechaResolucionAdopcion=" + fechaResolucionAdopcion + ", pet=" + pet
				+ ", solicitudAdopcion="
				+ solicitudAdopcion + "]";
	}

	public LocalDate getFechaPuestaEnAdopcion() {
		return fechaPuestaEnAdopcion;
	}

	public void setFechaPuestaEnAdopcion(LocalDate fechaPuestaEnAdopcion) {
		this.fechaPuestaEnAdopcion = fechaPuestaEnAdopcion;
	}

		public LocalDate getFechaResolucionAdopcion() {
		return fechaResolucionAdopcion;
	}

	public void setFechaResolucionAdopcion(LocalDate fechaResolucionAdopcion) {
		this.fechaResolucionAdopcion = fechaResolucionAdopcion;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public Set<SolicitudAdopcion> getSolicitudAdopcion() {
		return solicitudAdopcion;
	}

	public void setSolicitudAdopcion(Set<SolicitudAdopcion> solicitudAdopcion) {
		this.solicitudAdopcion = solicitudAdopcion;
	}



	

}

	