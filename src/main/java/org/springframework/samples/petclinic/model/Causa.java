package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "causas")
public class Causa extends BaseEntity {
	
	
	@Column(length = 100)
	@NotEmpty(message = "El nombre no puede estar vacio")
	private String nombre;
	
	@Column(length = 100)
	@NotEmpty(message = "El nombre de la organizacion no puede estar vacio")
	private String organizacion;
	
	@Column(length= 10000)
	@NotEmpty(message = "La descripcion no puede estar vacio")
	private String descripcion;
	
	@DecimalMin(value = "1.0", inclusive = true)
	@NotNull
	@Column(name = "recaudacion_objetivo")
	private Double recaudacionObjetivo;
	

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	public String getOrganizacion() {
		return this.organizacion;
	}

	public void setOrganizacion(final String organizacion) {
		this.organizacion = organizacion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(final String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getRecaudacionObjetivo() {
		return this.recaudacionObjetivo;
	}

	public void setRecaudacionObjetivo(final Double recaudacionObjetivo) {
		this.recaudacionObjetivo = recaudacionObjetivo;
	}
	
	
}
