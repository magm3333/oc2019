package com.example.demo.business;

import java.util.List;

import com.example.demo.business.exception.BusinessException;
import com.example.demo.model.Persona;

public interface IPersonaBusiness {
	
	public Persona add(Persona p) throws BusinessException;
	
	public List<Persona> listAll() throws BusinessException;
	
}
