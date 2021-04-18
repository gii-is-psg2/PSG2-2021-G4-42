package org.springframework.samples.petclinic.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
	
	@Column(length = 5500)
	@NotEmpty(message = "La descripcion no puede estar vacio")
	private String descripcion;
	
	@NotNull
	private Double recaudacion;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(String organizacion) {
		this.organizacion = organizacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getRecaudacion() {
		return recaudacion;
	}

	public void setRecaudacion(Double recaudacion) {
		this.recaudacion = recaudacion;
	}
	
	
}
