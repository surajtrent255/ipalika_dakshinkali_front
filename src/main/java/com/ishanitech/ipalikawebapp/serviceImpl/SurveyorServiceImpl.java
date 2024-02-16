package com.ishanitech.ipalikawebapp.serviceImpl;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ishanitech.ipalikawebapp.configs.properties.RestApiProperties;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.SurveyorDTO;
import com.ishanitech.ipalikawebapp.service.SurveyorService;
import com.ishanitech.ipalikawebapp.utilities.HttpUtils;

@Service
public class SurveyorServiceImpl implements SurveyorService {

	private final RestTemplate restTemplate;
	private final RestApiProperties restApiProperties;
	private final String SURVEYOR_BASE_URL = "/surveyor";
	
	public SurveyorServiceImpl(RestTemplate restTemplate, RestApiProperties restApiProperties) {
		this.restTemplate = restTemplate;
		this.restApiProperties = restApiProperties;
	}

	@Override
	public List<SurveyorDTO> getAllSurveyors() {
		String template = SURVEYOR_BASE_URL; 
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<List<SurveyorDTO>> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<SurveyorDTO>>> responseType = new ParameterizedTypeReference<Response<List<SurveyorDTO>>>() {};
		Response<List<SurveyorDTO>> surveyors = restTemplate.exchange(requestEntity, responseType).getBody();
		return surveyors.getData();
	}

}
