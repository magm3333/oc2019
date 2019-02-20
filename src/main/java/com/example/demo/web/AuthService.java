package com.example.demo.web;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.AuthToken;
import com.example.demo.business.IAuthTokenBusiness;
import com.example.demo.business.exception.BusinessException;
import com.example.demo.model.util.SimpleResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthService {

	@Autowired
	private IAuthTokenBusiness authTokenService;

	@GetMapping(value = "/token")
	public ResponseEntity<Object> getToken(@RequestParam("username") String username,
			@RequestParam("diasvalido") int diasvalido) {

		try {
			AuthToken token = buildToken(username, diasvalido);
			return new ResponseEntity<Object>("{\"token\":\"" + token.encodeCookieValue() + "\"}", HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Object>(new SimpleResponse(-1, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private AuthToken buildToken(String username, int diasvalido) throws BusinessException {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, diasvalido);
		AuthToken token = new AuthToken(username, c.getTime());
		authTokenService.save(token);
		return token;

	}
}
