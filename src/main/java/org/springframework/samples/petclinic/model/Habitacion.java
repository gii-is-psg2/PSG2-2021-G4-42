package org.springframework.samples.petclinic.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "habitacion")
public class Habitacion extends BaseEntity {

	@NotNull
	@Min(0)
	private Integer numero;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Reserva> reservas;

	
	public Set<Reserva> getReservas() {
		return this.reservas;
	}

	
	public void setReservas(final Set<Reserva> reservas) {
		this.reservas = reservas;
	}


	
	public Integer getNumero() {
		return this.numero;
	}


	
	public void setNumero(final Integer numero) {
		this.numero = numero;
	}
	
}
