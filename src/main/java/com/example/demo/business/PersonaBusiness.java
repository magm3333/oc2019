package com.example.demo.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.business.exception.BusinessException;
import com.example.demo.model.Persona;
import com.example.demo.model.persistence.PersonaRepository;

@Service
public class PersonaBusiness implements IPersonaBusiness {

	@Override
	public Persona add(Persona p) throws BusinessException {
		try {
			return personaDAO.save(p);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Autowired
	private PersonaRepository personaDAO;

	@Override
	public List<Persona> listAll() throws BusinessException {
		try {
			return personaDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

}
