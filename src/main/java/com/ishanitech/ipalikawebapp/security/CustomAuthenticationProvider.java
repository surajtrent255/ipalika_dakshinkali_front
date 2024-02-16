package com.ishanitech.ipalikawebapp.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ishanitech.ipalikawebapp.dto.LoginDTO;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.UserDTO;
import com.ishanitech.ipalikawebapp.service.RestClientService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private RestClientService restClient;
	
	@SuppressWarnings("unchecked")
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		LoginDTO loginDTO = new LoginDTO();
		String username = authentication.getName();
		loginDTO.setUsername(username);
		loginDTO.setPassword(authentication.getCredentials().toString());
		Response<UserDTO> loggedInUserResponse = null;
		loggedInUserResponse = (Response<UserDTO>) restClient.login(loginDTO);
		
		if((loggedInUserResponse != null) && (loggedInUserResponse.getData() != null)){
			UserDTO loggedInUser = loggedInUserResponse.getData();
			Collection<? extends GrantedAuthority> authorities =  loggedInUser.getRoles().stream().map(auth -> {
				return new SimpleGrantedAuthority(String.format("ROLE_%s", auth));
			}).collect(Collectors.toList());
			
			authentication = new UsernamePasswordAuthenticationToken(loggedInUser, null ,authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return authentication;
		} 
		
		throw new BadCredentialsException("Invalid Credentials");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}
	
}
