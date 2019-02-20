package com.example.demo;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.business.IAuthTokenBusiness;
import com.example.demo.business.exception.BusinessException;
import com.example.demo.model.business.NotFoundException;

//V0RWT25Gd25Qc01VL2V3M0lXV0VsZz09OktMaTFCZ2NhcFYrT01VR3FqNXl4cnc9PQ

public class CustomTokenAuthenticationFilter extends OncePerRequestFilter {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public CustomTokenAuthenticationFilter(IAuthTokenBusiness authTokenService) {
		super();
		this.authTokenService = authTokenService;
	}


	private IAuthTokenBusiness authTokenService;

	public static String ORIGIN_TOKEN_TOKEN = "token";
	public static String ORIGIN_TOKEN_HEADER = "header";

	public static String AUTH_HEADER = "X-AUTH-TOKEN";
	public static String AUTH_PARAMETER = "xauthtoken";
	
	//public static String ATTR_SESSION_NOT_CREATION = "ATTR_SESSION_NOT_CREATION";

	private boolean esValido(String valor) {
		return valor != null && valor.trim().length() > 10;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String parameter = request.getParameter(AUTH_PARAMETER);
		String header = request.getHeader(AUTH_HEADER);

		if (!esValido(parameter) && !esValido(header)) {
			chain.doFilter(request, response);
			return;
		}
		
		
		String token="";
		if (esValido(parameter)) {
			token=parameter;
			log.debug("Token recibido por query param="+token);
		} else {
			token=header;
			log.debug("Token recibido por header="+token);
		}
		String[] tokens = null;

		try {
			tokens = AuthToken.decode(token);
			if (tokens.length != 2) {
				chain.doFilter(request, response);
				return;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			chain.doFilter(request, response);
			return;
		}

		// A partir de aquí, se considera que se envió el el token y es propritario, por
		// ende si no está ok, login inválido
		AuthToken authToken = null;
		try {
			authToken = authTokenService.load(tokens[0]);
		} catch (NotFoundException e) {
			SecurityContextHolder.clearContext();
			throw new ServletException("No existe el token=" + token);
		} catch (BusinessException e) {
			SecurityContextHolder.clearContext();
			log.error(e.getMessage(), e);
			throw new ServletException(e);
		}

		if (!authToken.valid()) {
			try {
				if (authToken.getType().equals(AuthToken.TYPE_DEFAULT)
						|| authToken.getType().equals(AuthToken.TYPE_TO_DATE)
						|| authToken.getType().equals(AuthToken.TYPE_REQUEST_LIMIT)) {
					authTokenService.delete(authToken);
				}
				if (authToken.getType().equals(AuthToken.TYPE_FROM_TO_DATE)) {
					if (authToken.getTo().getTime() < System.currentTimeMillis()) {
						authTokenService.delete(authToken);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			SecurityContextHolder.clearContext();
			log.debug("El Token "+token+" ha expirado");
			throw new ServletException("El Token ha expirado. Token=" + token);
		}

		try {
			authToken.setLast_used(new Date());
			authToken.addRequest();
			authTokenService.save(authToken);

			String username = authToken.getUsername();
			
			log.debug("Token para usuario "+username);
			String[]  la="ADMIN".split(",");
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,AuthorityUtils.createAuthorityList(la));
			SecurityContextHolder.getContext().setAuthentication(auth);
			//request.setAttribute(ATTR_SESSION_NOT_CREATION, "true");
			
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			chain.doFilter(request, response);
		}

	}
	
	
	
	
	/*
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String parameter=request.getParameter("token");

		String username=null;
		String rol=null;
		if(parameter!=null && parameter.trim().length()>5) {
			if(parameter.equals("abcdef")) {
				username="admin";
				rol="ADMIN";
			}
			if(parameter.equals("abcdeg")) {
				username="user";
				rol="USER";
			}
			if(parameter.equals("abcdex")) {
				username="inventado";
				rol="LOQUESEA";
			}
		}
		
		if(username!=null) {
			String[]  la=rol.split(",");
			UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.createAuthorityList(la));
			SecurityContextHolder.getContext().setAuthentication(auth);
			log.debug("Usuario autenticado por token: {}",username);
		}
		
		chain.doFilter(request, response);
	}
*/
}
