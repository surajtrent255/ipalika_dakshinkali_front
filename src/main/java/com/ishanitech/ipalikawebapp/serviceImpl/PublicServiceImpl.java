package com.ishanitech.ipalikawebapp.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ishanitech.ipalikawebapp.configs.properties.RestApiProperties;
import com.ishanitech.ipalikawebapp.dto.ResidentDTO;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.service.PublicService;
import com.ishanitech.ipalikawebapp.utilities.HttpUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class PublicServiceImpl implements PublicService {
	private final String RESIDENT_BASE_URL = "public/resident";
	private final RestTemplate restTemplate;
	private final RestApiProperties restApiProperties;
	
	public PublicServiceImpl(RestTemplate restTemplate, RestApiProperties restApiProperties) {
		this.restTemplate = restTemplate;
		this.restApiProperties = restApiProperties;
	}

	@Override
	public Response<?> getResidentDataList() {
		String template = String.format("%s", RESIDENT_BASE_URL);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {};
		Response<List<ResidentDTO>> residents = restTemplate.exchange(requestEntity, responseType).getBody();
		return residents;
	}

	


	@Override
	public List<ResidentDTO> searchResidentByKey(HttpServletRequest request,String searchKey, String wardNo) {
		String template = String.format("%s/search", RESIDENT_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("queryParamName", "searchKey");
		uriVariables.put("keyword", searchKey);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("toleName", request.getParameter("toleName"));
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		String url = HttpUtils.createRequestUrlWithQueryString(restApiProperties, uriVariables);
		log.info("URL--->" + url);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}
	
	@Override
	public List<ResidentDTO> searchResidentByWard(HttpServletRequest request, String wardNo) {
		String template = String.format("%s/search/ward", RESIDENT_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		String url = HttpUtils.createRequestUrlWithWardString(restApiProperties, uriVariables);
		log.info("URL--->" + url);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}

	
	@Override
	public List<ResidentDTO> searchResidentByTole(HttpServletRequest request, String wardNo) {
		String template = String.format("%s/search/tole", RESIDENT_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("toleName", request.getParameter("toleName"));
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		
		String url = HttpUtils.createRequestUrlWithToleString(restApiProperties, uriVariables);
		System.out.println("seachByTole ---->" + request.getParameter("toleName"));
		log.info("URL--->" + url);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}

	@Override
	public List<ResidentDTO> getResidentByPageLimit(HttpServletRequest request, String wardNo) {
		String template = String.format("%s/pageLimit", RESIDENT_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("toleName", request.getParameter("toleName"));
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("searchKey", request.getParameter("searchKey"));
		uriVariables.put("lastSeenId", request.getParameter("lastSeenId"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		
		String url = HttpUtils.createRequestUrlWithPageLimit(restApiProperties, uriVariables);
		log.info("URL--->" + url);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}
	

	@Override
	public List<ResidentDTO> getNextLotResidents(HttpServletRequest request) {
		String endPoint = "?";
		if(request.getParameter("searchKey") != null) {
			endPoint += "searchKey=" + request.getParameter("searchKey") + "&";
		}
		if(request.getParameter("wardNo") != null) {
			endPoint += "wardNo=" + request.getParameter("wardNo") + "&";
		}
		if(request.getParameter("toleName") != null) {
			endPoint += "toleName=" + request.getParameter("toleName") + "&";
		}
		if(request.getParameter("sortBy") != null) {
			endPoint += "sortBy=" + request.getParameter("sortBy") + "&";
			endPoint += "sortByOrder=" + request.getParameter("sortByOrder") + "&";
		}
		
		endPoint += "last_seen=" + request.getParameter("lastSeenId") + "&";
		endPoint += "action=" + request.getParameter("action") + "&";
		endPoint += "currentPage=" + request.getParameter("currentPage") + "&";
		endPoint += "pageSize=" + request.getParameter("pageLimit");
		log.info("SearchKEy---->"+ request.getParameter("searchKey"));
		log.info("WardNo---->"+ request.getParameter("wardNo"));
		log.info("PageLimit---->"+ request.getParameter("pageLimit"));
		String template = String.format("%s/nextLot" + endPoint, RESIDENT_BASE_URL);
		
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, uriVariables);
		log.info("URL--->" + url);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}

	@Override
	public List<ResidentDTO> getSortedResidents(HttpServletRequest request ) {
		String endPoint = "?";
		if(request.getParameter("searchKey") != null) {
			endPoint += "searchKey=" + request.getParameter("searchKey") + "&";
		}
		if(request.getParameter("wardNo") != null) {
			endPoint += "wardNo=" + request.getParameter("wardNo") + "&";
		}
		if(request.getParameter("toleName") != null) {
			endPoint += "toleName=" + request.getParameter("toleName") + "&";
		}
		//endPoint += "last_seen=" + request.getParameter("lastSeenId") + "&";
		//endPoint += "action=" + request.getParameter("action") + "&";
		endPoint += "sortBy=" + request.getParameter("sortBy") + "&";
		endPoint += "sortByOrder=" + request.getParameter("sortByOrder") + "&";
		endPoint += "pageSize=" + request.getParameter("pageLimit");
		
		String template = String.format("%s/sortBy" + endPoint, RESIDENT_BASE_URL);
		
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, uriVariables);
		log.info("URL--->" + url);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}

	@Override
	public String getTotalHouseCountByWard(String wardNo, String toleName) {
		
		if(toleName != null) {
			System.out.println("tolex name before--->" + toleName);
			toleName = toleName.replace(" ", "spacex");
			System.out.println("tolex name after--->" + toleName);
		}
		
		String template = String.format("ward/totalHouseCount/" + wardNo + "?toleName=" + toleName);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, uriVariables);
		System.out.println("HouseCountURl--->" + url);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<Integer>> responseType = new ParameterizedTypeReference<Response<Integer>>() {
		};
		Integer totalHouseCount = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return totalHouseCount.toString();
	}



}
