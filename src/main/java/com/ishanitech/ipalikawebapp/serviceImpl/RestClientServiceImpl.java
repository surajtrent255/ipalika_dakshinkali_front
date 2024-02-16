/**
 * Created By: Pujan
 * Date: 2020/1/21 (yyyy/mm/dd)
 */
package com.ishanitech.ipalikawebapp.serviceImpl;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishanitech.ipalikawebapp.dto.LoginDTO;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.UserDTO;
import com.ishanitech.ipalikawebapp.service.RestClientService;
import com.ishanitech.ipalikawebapp.utilities.UserDetailsUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RestClientServiceImpl implements RestClientService {
	
    private RestTemplate restClient;
    
    @Autowired
    ObjectMapper objectMapper;
    
    private final String loginUrl = "/login";

    
    private final String baseUrl = "http://localhost:8888";
    
    //@Autowired is optional. Even when @Autowired is not specified
    //Spring boot automatically gets the bean for constructor injection
    @Autowired
    public RestClientServiceImpl(RestTemplate restTemplate) {
        this.restClient = restTemplate;
    }



    @Override
    public Response<UserDTO> login(LoginDTO requestObject) {
        Response<UserDTO> response = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginDTO> entity = new HttpEntity<>(requestObject, headers);
        ResponseEntity<Response> result = null; 
        try {
        	result = (ResponseEntity<Response>) restClient.postForEntity(baseUrl + loginUrl, entity, Response.class);
        	response = objectMapper.convertValue(result.getBody(), new TypeReference<Response<UserDTO>>() {
			});
        	response.getData().setToken(result.getHeaders().getFirst("Authorization"));
        } catch(HttpStatusCodeException sce) {
        	throw new BadCredentialsException("Bad Credentials");
        } catch(RestClientException rce) {
        	log.info("INSIDE LOGIN CALL: " + rce.getMessage());
        } 
        return response;
    }

    /**
     *
     * @param url
     * @param responseType
     * @return
     */


    @Override
    public Response<?> getData(String url, JavaType responseType) {
        Response<?> response = null;
        URI completeUrl = URI.create(baseUrl + url);
        HttpHeaders headers = new HttpHeaders();
        UserDTO loggedInUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(UserDetailsUtil.isLoggedIn(authentication)) {
            loggedInUser = (UserDTO) authentication.getPrincipal();
        }
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("parameters" , headers);
        ResponseEntity<String> responseEntity = null;
        try {
        	responseEntity = restClient.exchange(completeUrl, HttpMethod.GET, entity, String.class);
        	response = objectMapper.readValue(responseEntity.getBody(), responseType);
        } catch(HttpStatusCodeException sce) {
        	log.error("ERROR CODE: " + sce.getStatusCode());
        	try {
                response = objectMapper.readValue(sce.getResponseBodyAsString(), responseType);
            } catch (IOException e) {
                log.error("ERROR: " + e.getMessage());
            }
        } catch(RestClientException rce) {
        	log.error("ERROR MSG: " + rce.getMessage());
        } catch(IOException ioEx) {
        	log.error("ERROR: " + ioEx.getMessage());
        }
        
        return response;
    }
}
