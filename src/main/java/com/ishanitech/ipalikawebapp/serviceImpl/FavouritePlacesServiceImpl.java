package com.ishanitech.ipalikawebapp.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ishanitech.ipalikawebapp.dto.ResponseFavPlace;
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
import com.ishanitech.ipalikawebapp.dto.FavouritePlaceDTO;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.service.FavouritePlacesService;
import com.ishanitech.ipalikawebapp.utilities.HttpUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class FavouritePlacesServiceImpl implements FavouritePlacesService {
	private final RestTemplate restTemplate;
	private final RestApiProperties restApiProperties;
	private final String FAVOURITE_PLACE_BASE_URL = "/favourite-place";
	
	public FavouritePlacesServiceImpl(RestTemplate restTemplate, RestApiProperties restApiProperties) {
		this.restTemplate = restTemplate;
		this.restApiProperties = restApiProperties;
	}

	@Override
	public ResponseFavPlace<List<FavouritePlaceDTO>> getAllFavouritePlaces() {
		String template = FAVOURITE_PLACE_BASE_URL; 
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<List<FavouritePlaceDTO>> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<ResponseFavPlace<List<FavouritePlaceDTO>>> responseType = new ParameterizedTypeReference<ResponseFavPlace<List<FavouritePlaceDTO>>>() {};
		ResponseFavPlace<List<FavouritePlaceDTO>> favouritePlaces = restTemplate.exchange(requestEntity, responseType).getBody();
		return favouritePlaces;
	}
	
	@Override
	public Response<List<FavouritePlaceDTO>> getAllFavouritePlacesBySurveyor(int userId) {
		String template = FAVOURITE_PLACE_BASE_URL + "/surveyor/" + userId; 
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<List<FavouritePlaceDTO>> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>> responseType = new ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>>() {};
		Response<List<FavouritePlaceDTO>> favouritePlaces = restTemplate.exchange(requestEntity, responseType).getBody();
		return favouritePlaces;
	}

	@Override
	public Response<FavouritePlaceDTO> getFavouritePlaceByPlaceId(String placeId) {
		String template = String.format("%s/%s", FAVOURITE_PLACE_BASE_URL, placeId);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<FavouritePlaceDTO> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<FavouritePlaceDTO>> responseType = new ParameterizedTypeReference<Response<FavouritePlaceDTO>>() {};
		Response<FavouritePlaceDTO> favouritePlaceInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return favouritePlaceInfo;
	}

	@Override
	public void deleteFavouritePlacebyPlaceId(String favPlaceId, String token) {
		String template = String.format("%s/%s", FAVOURITE_PLACE_BASE_URL, favPlaceId);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<String> requestEntity = HttpUtils.createRequestEntity(HttpMethod.DELETE, MediaType.APPLICATION_JSON, token , url);
		restTemplate.exchange(requestEntity, String.class);
	}

	@Override
	public void addFavouritePlaceInfo(FavouritePlaceDTO favouritePlaceInfo, String token) {
		String template = String.format("%s/single", FAVOURITE_PLACE_BASE_URL);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<FavouritePlaceDTO> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, favouritePlaceInfo, MediaType.APPLICATION_JSON, token, url);
		restTemplate.exchange(requestEntity, String.class);
	}

	@Override
	public void addFavouritePlaceImage(MultipartFile file, String imageName, String token) {
		String template = String.format("%s/image", FAVOURITE_PLACE_BASE_URL);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("picture", file.getResource());
		HttpHeaders headers = HttpUtils.createHeader(MediaType.MULTIPART_FORM_DATA, token);
	    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
		restTemplate.postForLocation(url, requestEntity);
	}

	@Override
	public List<String> getTypesofFavourtiePlaces() {
		String template = String.format("%s/type", FAVOURITE_PLACE_BASE_URL);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<List<String>> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<String>>> responseType = new ParameterizedTypeReference<Response<List<String>>>() {};
		Response<List<String>> favPlaceTypes = restTemplate.exchange(requestEntity, responseType).getBody();
		return favPlaceTypes.getData();
	}

	@Override
	public void editFavouritePlaceInfo(FavouritePlaceDTO favPlaceInfo, String favPlaceId, String token) {
		String template = String.format("%s/{favPlaceId}", FAVOURITE_PLACE_BASE_URL);
		Map<String, Object> urlValues = new HashMap<>();
		urlValues.put("favPlaceId", favPlaceId);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, urlValues);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.PUT, favPlaceInfo, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {};
		restTemplate.exchange(requestEntity, responseType);
	}

	@Override
	public void addEditedFavouritePlaceImage(MultipartFile file, String imageName, String token) {
		String template = String.format("%s/editImage", FAVOURITE_PLACE_BASE_URL);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("picture", file.getResource());
		HttpHeaders headers = HttpUtils.createHeader(MediaType.MULTIPART_FORM_DATA, token);
	    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
		restTemplate.postForLocation(url, requestEntity);
		
	}

	@Override
	public ResponseFavPlace<List<FavouritePlaceDTO>> searchFavouritePlaceByWard(HttpServletRequest request, String wardNo) {
		String template = String.format("%s/search/ward", FAVOURITE_PLACE_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		uriVariables.put("placeType", request.getParameter("placeType"));
		uriVariables.put("surveyor", request.getParameter("surveyor"));
		String url = HttpUtils.createRequestUrlWithWardStringFavPlace(restApiProperties, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<ResponseFavPlace<List<FavouritePlaceDTO>>> responseType = new ParameterizedTypeReference<ResponseFavPlace<List<FavouritePlaceDTO>>>() {
		};
		ResponseFavPlace<List<FavouritePlaceDTO>> favouritePlaces = restTemplate.exchange(requestEntity, responseType).getBody();
		return favouritePlaces;
	}

	@Override
	public List<FavouritePlaceDTO> searchFavouritePlaceByKey(HttpServletRequest request, String searchKey, String wardNo, String placeType) {
		String template = String.format("%s/search", FAVOURITE_PLACE_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("queryParamName", "searchKey");
		uriVariables.put("keyword", searchKey);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("placeType", placeType);
		uriVariables.put("surveyor", request.getParameter("surveyor"));
		
		String url = HttpUtils.createRequestUrlWithQueryStringFavPlace(restApiProperties, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>> responseType = new ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>>() {
		};
		List<FavouritePlaceDTO> favouritePlaces = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return favouritePlaces;
	}

	@Override
	public List<FavouritePlaceDTO> searchFavouritePlaceByPlaceType(HttpServletRequest request, String placeType) {
		String template = String.format("%s/search/placeType", FAVOURITE_PLACE_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", request.getParameter("wardNo"));
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		uriVariables.put("placeType", placeType);
		uriVariables.put("surveyor", request.getParameter("surveyor"));
		
		String url = HttpUtils.createRequestUrlWithWardStringFavPlace(restApiProperties, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>> responseType = new ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>>() {
		};
		List<FavouritePlaceDTO> favouritePlaces = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return favouritePlaces;
	}
	
	
	@Override
	public List<FavouritePlaceDTO> searchFavouritePlaceBySurveyor(HttpServletRequest request, String surveyor) {
		String template = String.format("%s/search/surveyor", FAVOURITE_PLACE_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", request.getParameter("wardNo"));
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		uriVariables.put("placeType", request.getParameter("placeType"));
		uriVariables.put("surveyor", surveyor);
		
		String url = HttpUtils.createRequestUrlWithWardStringFavPlace(restApiProperties, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>> responseType = new ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>>() {
		};
		List<FavouritePlaceDTO> favouritePlaces = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return favouritePlaces;
	}

	@Override
	public List<FavouritePlaceDTO> getFavouritePlaceForGoBack(HttpServletRequest request) {
		String template = String.format("%s/forGoBack", FAVOURITE_PLACE_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", request.getParameter("wardNo"));
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("searchKey", request.getParameter("searchKey"));
		uriVariables.put("lastSeenId", request.getParameter("lastSeenId"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		uriVariables.put("placeType", request.getParameter("placeType"));
		uriVariables.put("surveyor", request.getParameter("surveyor"));
		String url = HttpUtils.createRequestUrlWithPageLimitFavPlace(restApiProperties, uriVariables);
		url +="&lastPageItemCount="+request.getParameter("lastPageItemCount");
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>> responseType = new ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>>() {
		};
		List<FavouritePlaceDTO> favouritePlaces = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return favouritePlaces;
	}

	@Override
	public List<FavouritePlaceDTO> getFavouritePlaceByPageLimit(HttpServletRequest request, String wardNo) {
		String template = String.format("%s/pageLimit", FAVOURITE_PLACE_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("searchKey", request.getParameter("searchKey"));
		uriVariables.put("lastSeenId", request.getParameter("lastSeenId"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		uriVariables.put("placeType", request.getParameter("placeType"));
		uriVariables.put("surveyor", request.getParameter("surveyor"));
		
		String url = HttpUtils.createRequestUrlWithPageLimitFavPlace(restApiProperties, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>> responseType = new ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>>() {
		};
		List<FavouritePlaceDTO> favouritePlaces = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return favouritePlaces;
	}

	@Override
	public List<FavouritePlaceDTO> getNextLotFavouritePlace(HttpServletRequest request) {
		String endPoint = "?";
		if(request.getParameter("searchKey") != null) {
			endPoint += "searchKey=" + request.getParameter("searchKey") + "&";
		}
		if(request.getParameter("wardNo") != null) {
			endPoint += "wardNo=" + request.getParameter("wardNo") + "&";
		}
		if(request.getParameter("placeType") != null) {
			endPoint += "placeType=" + request.getParameter("placeType") + "&";
		}
		if(request.getParameter("surveyor") != null) {
			endPoint += "surveyor=" + request.getParameter("surveyor") + "&";
		}
		if(request.getParameter("sortBy") != null) {
			endPoint += "sortBy=" + request.getParameter("sortBy") + "&";
			endPoint += "sortByOrder=" + request.getParameter("sortByOrder") + "&";
		}
		
		endPoint += "last_seen=" + request.getParameter("lastSeenId") + "&";
		endPoint += "action=" + request.getParameter("action") + "&";
		endPoint += "currentPage=" + request.getParameter("currentPage") + "&";
		endPoint += "pageSize=" + request.getParameter("pageLimit");
		String template = String.format("%s/nextLot" + endPoint, FAVOURITE_PLACE_BASE_URL);
		
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>> responseType = new ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>>() {
		};
		List<FavouritePlaceDTO> favouritePlaces = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return favouritePlaces;
	}

	@Override
	public List<FavouritePlaceDTO> getSortedFavouritePlace(HttpServletRequest request) {
		String endPoint = "?";
		if(request.getParameter("searchKey") != null) {
			endPoint += "searchKey=" + request.getParameter("searchKey") + "&";
		}
		if(request.getParameter("wardNo") != null) {
			endPoint += "wardNo=" + request.getParameter("wardNo") + "&";
		}
		if(request.getParameter("placeType") != null) {
			endPoint += "placeType=" + request.getParameter("placeType") + "&";
		}
		if(request.getParameter("surveyor") != null) {
			endPoint += "surveyor=" + request.getParameter("surveyor") + "&";
		}
		endPoint += "sortBy=" + request.getParameter("sortBy") + "&";
		endPoint += "sortByOrder=" + request.getParameter("sortByOrder") + "&";
		endPoint += "pageSize=" + request.getParameter("pageLimit");
		
		String template = String.format("%s/sortBy" + endPoint, FAVOURITE_PLACE_BASE_URL);
		
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>> responseType = new ParameterizedTypeReference<Response<List<FavouritePlaceDTO>>>() {
		};
		List<FavouritePlaceDTO> favouritePlaces = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return favouritePlaces;
	}



}
