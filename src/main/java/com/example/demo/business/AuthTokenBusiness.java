package com.example.demo.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.AuthToken;
import com.example.demo.business.exception.BusinessException;
import com.example.demo.model.business.NotFoundException;
import com.example.demo.model.persistence.AuthTokenRespository;

@Service
public class AuthTokenBusiness implements IAuthTokenBusiness{


	@Autowired
	private AuthTokenRespository authTokenDAO;
	
	@Override
	public AuthToken save(AuthToken at) throws BusinessException {
		try {
			return authTokenDAO.save(at);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public AuthToken load(String series) throws BusinessException, NotFoundException {
		Optional<AuthToken> atO;
		try {
			atO = authTokenDAO.findById(series);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!atO.isPresent())
			throw new NotFoundException("No se encuentra el token de autenticación serie=" + series);
		return atO.get();
	}

	@Override
	public void delete(AuthToken at) throws BusinessException {
		try {
			authTokenDAO.delete(at);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
	}

}
