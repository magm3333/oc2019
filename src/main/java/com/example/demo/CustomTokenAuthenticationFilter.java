package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class CustomTokenAuthenticationFilter extends OncePerRequestFilter {
	private Logger log = LoggerFactory.getLogger(this.getClass());
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

}
