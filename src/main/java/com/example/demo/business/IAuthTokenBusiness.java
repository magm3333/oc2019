package com.example.demo.business;


import com.example.demo.AuthToken;
import com.example.demo.business.exception.BusinessException;
import com.example.demo.model.business.NotFoundException;

public interface IAuthTokenBusiness {
	public AuthToken save(AuthToken at) throws BusinessException;

	public AuthToken load(String series) throws BusinessException, NotFoundException;

	public void delete(AuthToken at) throws BusinessException;
}
