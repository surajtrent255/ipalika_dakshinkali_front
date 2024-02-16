/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Feb 1, 2020
 */
package com.ishanitech.ipalikawebapp.serviceImpl;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ishanitech.ipalikawebapp.configs.properties.RestApiProperties;
import com.ishanitech.ipalikawebapp.dto.FormDetail;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.ToleDTO;
import com.ishanitech.ipalikawebapp.service.FormService;
import com.ishanitech.ipalikawebapp.utilities.HttpUtils;

@Service
public class FormServiceImpl implements FormService {
	private final RestTemplate restTemplate;
	private final RestApiProperties restApiProperties;
	private final String FORM_BASE_URL = "/form-detail";
	
	public FormServiceImpl(RestTemplate restTemplate, RestApiProperties restApiProperties) {
		this.restTemplate = restTemplate;
		this.restApiProperties = restApiProperties;
	}
	
	@Override
	public List<FormDetail> getFullFormDetailById(int id, String token) {
		String template = FORM_BASE_URL + "/" + id;
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, token, url); 
		ParameterizedTypeReference<Response<List<FormDetail>>> responseType = new ParameterizedTypeReference<Response<List<FormDetail>>>() {};
		Response<List<FormDetail>> formDetail = restTemplate.exchange(requestEntity, responseType).getBody();
		return formDetail.getData();
	}

	@Override
	public Response<List<String>> getListofDistricts(String token) {
		String template = FORM_BASE_URL + "/districts";
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<String>>> responseType = new ParameterizedTypeReference<Response<List<String>>>() {};
		Response<List<String>> districts = restTemplate.exchange(requestEntity, responseType).getBody();
		return districts;
	}

	@Override
	public Response<List<String>> getListOfWards(String token) {
		String template = "/ward";
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<String>>> responseType = new ParameterizedTypeReference<Response<List<String>>>() {};
		Response<List<String>> wards = restTemplate.exchange(requestEntity, responseType).getBody();
		return wards;
	}

	@Override
	public Response<List<ToleDTO>> getListOfToles(String token) {
		String template = "/ward/toles";
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<ToleDTO>>> responseType = new ParameterizedTypeReference<Response<List<ToleDTO>>>() {};
		Response<List<ToleDTO>> toles = restTemplate.exchange(requestEntity, responseType).getBody();
		return toles;
	}
}
