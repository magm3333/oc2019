package com.example.demo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Persona implements Serializable{
	

	private static final long serialVersionUID = -7345353678771559861L;

	@Column(length=100)
	private String apellido;
	
	@Id
	private int dni;

	private Date fechaNacimiento;

	@Column(length=100)
	private String nombre;

	public String getApellido() {
		return apellido;
	}

	public int getDni() {
		return dni;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setDni(int dni) {
		this.dni = dni;
	}
	
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
