package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.IPersonaBusiness;
import com.example.demo.business.exception.BusinessException;
import com.example.demo.model.Persona;

@RestController
@RequestMapping("/api/v1/personas")
public class PersonaService {

	@Autowired
	private IPersonaBusiness personaBusiness;
	
	@PostMapping("")
	public ResponseEntity<Persona> add(@RequestBody Persona persona) {
		try {
			return new ResponseEntity<Persona>(personaBusiness.add(persona),HttpStatus.CREATED);
		} catch (BusinessException e) {
			return new ResponseEntity<Persona>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("")
	public ResponseEntity<List<Persona>> list() {
		try {
			return new ResponseEntity<List<Persona>>(personaBusiness.listAll(),HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<List<Persona>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}
