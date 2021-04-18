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
}
