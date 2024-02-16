/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Feb 18, 2020
 */
package com.ishanitech.ipalikawebapp.serviceImpl;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ishanitech.ipalikawebapp.configs.properties.RestApiProperties;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.ToleDTO;
import com.ishanitech.ipalikawebapp.dto.WardDTO;
import com.ishanitech.ipalikawebapp.service.WardService;
import com.ishanitech.ipalikawebapp.utilities.HttpUtils;

@Service
public class WardServiceImpl implements WardService {
	private final RestApiProperties restApiProperties;
	private final RestTemplate restTemplate;
	private final String BASE_WARD_ADDRESS = "/ward";
	public WardServiceImpl(RestTemplate restTemplate, RestApiProperties restApiProperties) {
		this.restTemplate = restTemplate;
		this.restApiProperties = restApiProperties;
	}

	@Override
	public List<Integer> getAllWards() {
		String url = HttpUtils.createRequestUrl(restApiProperties, BASE_WARD_ADDRESS, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<Integer>>> responseType = new ParameterizedTypeReference<Response<List<Integer>>>() {
		};
		return restTemplate.exchange(requestEntity, responseType).getBody().getData();
	}

	@Override
	public void addWard(WardDTO wardInfo, String token) {
		String template = BASE_WARD_ADDRESS;
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, wardInfo, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
		};
		restTemplate.exchange(requestEntity, responseType);
	}

	@Override
	public Response<WardDTO> getWardByWardNumber(int wardNo, String token) {
		String template = String.format("%s/%s", BASE_WARD_ADDRESS, wardNo);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<WardDTO>> responseType = new ParameterizedTypeReference<Response<WardDTO>>() {};
		Response<WardDTO> wardInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return wardInfo;
	}

	@Override
	public Response<List<WardDTO>> getAllWardInfo(String token) {
		String template = String.format("%s/%s", BASE_WARD_ADDRESS, "all");
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<WardDTO>>> responseType = new ParameterizedTypeReference<Response<List<WardDTO>>>() {};
		Response<List<WardDTO>> wardListInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return wardListInfo;
	}

	@Override
	public void deleteWard(int wardNo, String token) {
		String template = String.format("%s/%s", BASE_WARD_ADDRESS, wardNo);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.DELETE, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {};
		restTemplate.exchange(requestEntity, responseType);
	}

	@Override
	public void editWard(WardDTO wardInfo, int wardNo, String token) {
		String template = String.format("%s/%s", BASE_WARD_ADDRESS, wardNo);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.PUT, wardInfo, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
		};
		restTemplate.exchange(requestEntity, responseType);
	}

	@Override
	public void addWardBuildingImage(MultipartFile file, String imageName, String token) {
		String template = String.format("%s/image", BASE_WARD_ADDRESS);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("picture", file.getResource());
		HttpHeaders headers = HttpUtils.createHeader(MediaType.MULTIPART_FORM_DATA, token);
	    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
		restTemplate.postForLocation(url, requestEntity);
		
	}
	
	@Override
	public Response<List<ToleDTO>> getListOfToles() {
		String template = "/ward/toles";
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<ToleDTO>>> responseType = new ParameterizedTypeReference<Response<List<ToleDTO>>>() {};
		Response<List<ToleDTO>> toles = restTemplate.exchange(requestEntity, responseType).getBody();
		return toles;
	}
	
}
