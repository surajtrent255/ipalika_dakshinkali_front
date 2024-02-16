package com.ishanitech.ipalikawebapp.service;

import com.fasterxml.jackson.databind.JavaType;
import com.ishanitech.ipalikawebapp.dto.LoginDTO;
import com.ishanitech.ipalikawebapp.dto.Response;

public interface RestClientService {
	Response<?> login(LoginDTO requestObject);

	Response<?> getData(String url, JavaType responseType);
}
