package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "donaciones")
public class Donacion extends BaseEntity {
	@NotNull
	@Column(name = "fecha_don")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate fechaDonacion;
	
	@NotNull
	@Column(name = "cantidad")
	private Double cantidadDonada;
	
	@ManyToOne
	@JoinColumn(name = "id_causa")
	@NotNull
	private Causa causa;
	
	@ManyToOne
	@JoinColumn(name = "id_owner")
	@NotNull
	private Owner donante;

	
	public LocalDate getFechaDonacion() {
		return this.fechaDonacion;
	}

	
	public void setFechaDonacion(final LocalDate fechaDonacion) {
		this.fechaDonacion = fechaDonacion;
	}

	
	public Double getCantidadDonada() {
		return this.cantidadDonada;
	}

	
	public void setCantidadDonada(final Double cantidadDonada) {
		this.cantidadDonada = cantidadDonada;
	}

	
	public Causa getCausa() {
		return this.causa;
	}

	
	public void setCausa(final Causa causa) {
		this.causa = causa;
	}

	
	public Owner getDonante() {
		return this.donante;
	}

	
	public void setDonante(final Owner donante) {
		this.donante = donante;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.cantidadDonada == null) ? 0 : this.cantidadDonada.hashCode());
		result = prime * result + ((this.causa == null) ? 0 : this.causa.hashCode());
		result = prime * result + ((this.donante == null) ? 0 : this.donante.hashCode());
		result = prime * result + ((this.fechaDonacion == null) ? 0 : this.fechaDonacion.hashCode());
		return result;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Donacion other = (Donacion) obj;
		return other.getId().equals(this.getId());
	}
	
	
}
