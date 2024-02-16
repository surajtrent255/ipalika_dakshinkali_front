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
import com.ishanitech.ipalikawebapp.dto.AnswerDTO;
import com.ishanitech.ipalikawebapp.dto.FamilyMemberDTO;
import com.ishanitech.ipalikawebapp.dto.MemberFormDetailsDTO;
import com.ishanitech.ipalikawebapp.dto.ResidentDTO;
import com.ishanitech.ipalikawebapp.dto.ResidentDetailDTO;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.RoleWardDTO;
import com.ishanitech.ipalikawebapp.service.ResidentService;
import com.ishanitech.ipalikawebapp.utilities.HttpUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ResidentServiceImpl implements ResidentService {
	private final String RESIDENT_BASE_URL = "resident";
	private final RestTemplate restTemplate;
	private final RestApiProperties restApiProperties;
	public ResidentServiceImpl(RestTemplate restTemplate, RestApiProperties restApiProperties) {
		this.restTemplate = restTemplate;
		this.restApiProperties = restApiProperties;
	}

	@Override
	public Response<?> getResidentDataList(String token, List<String> roles, int wardNumber, HttpServletRequest httpServletRequest) {
		
		RoleWardDTO roleWard = new RoleWardDTO();
		if(roles.contains("WARD_ADMIN")) {
			roleWard.setRole(3);
			roleWard.setWardNumber(wardNumber);
		}
		boolean goBack = false;
		if(httpServletRequest.getParameter("backFrom") != null){
            goBack = !httpServletRequest.getParameter("backFrom").trim().isEmpty();
		}

        Integer prevLastItemId = httpServletRequest.getParameter("prevLastItemId") != null ?
				Integer.parseInt(httpServletRequest.getParameter("prevLastItemId")) : 0;
		Integer lastSeenId = httpServletRequest.getParameter("lastSeenId") != null ? Integer.parseInt(httpServletRequest.getParameter("lastSeenId")):0;
		String searchKey = httpServletRequest.getParameter("searchKey") != null ? httpServletRequest.getParameter("searchKey"):"";
		Integer wardNo = httpServletRequest.getParameter("wardNo") != null ? Integer.parseInt(httpServletRequest.getParameter("wardNo")) : 0;
		String toleName =  httpServletRequest.getParameter("toleName") != null ? httpServletRequest.getParameter("toleName"):"";;
		Integer currentPage = httpServletRequest.getParameter("currentPage") != null ? Integer.parseInt(httpServletRequest.getParameter("currentPage")):0;;
		Integer pageLimit = httpServletRequest.getParameter("pageLimit") != null ? Integer.parseInt(httpServletRequest.getParameter("pageLimit")):0;;
		String backFrom = httpServletRequest.getParameter("backFrom") != null ? httpServletRequest.getParameter("backFrom"): "";;
		roleWard.setUrlObject(new RoleWardDTO.UrlObject(goBack, wardNo, prevLastItemId,lastSeenId, searchKey, toleName, currentPage, pageLimit, backFrom));
		String template = String.format("%s", RESIDENT_BASE_URL);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, roleWard , MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {};
		Response<List<ResidentDTO>> residents = restTemplate.exchange(requestEntity, responseType).getBody();
		return residents;
	}

	@Override
	public Response<?> getResidentFullDetail(String filledId, String token) {
		String template = String.format("%s/detail/{filledId}", RESIDENT_BASE_URL);
		Map<String, Object> urlValues = new HashMap<>();
		urlValues.put("filledId", filledId);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, urlValues);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, null, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<ResidentDetailDTO>> responseType = new ParameterizedTypeReference<Response<ResidentDetailDTO>>() {};
		Response<ResidentDetailDTO> fullDetail = restTemplate.exchange(requestEntity, responseType).getBody();
		return fullDetail;
	}
	
	@Override
	public Response<?> getResidentFullDetailRaw(String filledId, String token) {
		String template = String.format("%s/detail/rawAnswers/{filledId}", RESIDENT_BASE_URL);
		Map<String, Object> urlValues = new HashMap<>();
		urlValues.put("filledId", filledId);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, urlValues);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, null, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<AnswerDTO>> responseType = new ParameterizedTypeReference<Response<AnswerDTO>>() {};
		Response<AnswerDTO> fullDetail = restTemplate.exchange(requestEntity, responseType).getBody();
		return fullDetail;
	}

	@Override
	public void addFamilyMember(FamilyMemberDTO familyMemberInfo, String token) {
		String template = String.format("%s/member", RESIDENT_BASE_URL);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, familyMemberInfo, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
		};
		restTemplate.exchange(requestEntity, responseType);
	}

	@Override
	public Response<?> getMemberFormDetails(String token) {
		String template = String.format("%s/memberFormDetails", RESIDENT_BASE_URL);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<MemberFormDetailsDTO>> responseType = new ParameterizedTypeReference<Response<MemberFormDetailsDTO>>() {
		};
		return restTemplate.exchange(requestEntity, responseType).getBody();
	}

	@Override
	public List<ResidentDTO> searchResidentByKey(HttpServletRequest request,String searchKey, String wardNo, String token) {
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
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}
	
	@Override
	public List<ResidentDTO> searchResidentByWard(HttpServletRequest request, String wardNo, String token) {
		String template = String.format("%s/search/ward", RESIDENT_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		String url = HttpUtils.createRequestUrlWithWardString(restApiProperties, uriVariables);
		log.info("URL--->" + url);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}

	
	@Override
	public List<ResidentDTO> searchResidentByTole(HttpServletRequest request, String wardNo, String token) {
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
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}

	@Override
	public List<ResidentDTO> getResidentByPageLimit(HttpServletRequest request, String wardNo, String token) {
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
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}
	
	@Override
	public Response<?> getMemberByMemberId(String token, String memberId) {
		String template = String.format("%s/member/{memberId}", RESIDENT_BASE_URL);
		Map<String, Object> urlValues = new HashMap<>();
		urlValues.put("memberId", memberId);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, urlValues);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<FamilyMemberDTO>> responseType = new ParameterizedTypeReference<Response<FamilyMemberDTO>>() {};
		Response<FamilyMemberDTO> memberInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return memberInfo;
	}

	@Override
	public void editFamilyMemberInfo(FamilyMemberDTO familyMemberInfo, String memberId, String token) {
		String template = String.format("%s/member/{memberId}", RESIDENT_BASE_URL);
		Map<String, Object> urlValues = new HashMap<>();
		urlValues.put("memberId", memberId);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, urlValues);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.PUT, familyMemberInfo, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
		};
		restTemplate.exchange(requestEntity, responseType);
		
	}

	@Override
	public void deleteFamilyMember(String memberId, String token) {
		String template = String.format("%s/member/{memberId}", RESIDENT_BASE_URL);
		Map<String, Object> urlValues = new HashMap<>();
		urlValues.put("memberId", memberId);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, urlValues);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.DELETE, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {};
		restTemplate.exchange(requestEntity, responseType);
	}

	@Override
	public void deleteHouseholdByFamilyId(String familyId, String token) {
		String template = String.format("%s/{familyId}", RESIDENT_BASE_URL);
		Map<String, Object> urlValues = new HashMap<>();
		urlValues.put("familyId", familyId);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, urlValues);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.DELETE, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {};
		restTemplate.exchange(requestEntity, responseType);
		
		
		
	}

	@Override
	public List<ResidentDTO> getNextLotResidents(HttpServletRequest request,List<String> roles, int wardNumber, String token) {
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
		
		//for roleWard
		RoleWardDTO roleWard = new RoleWardDTO();
		if(roles.contains("WARD_ADMIN")) {
			roleWard.setRole(3);
			roleWard.setWardNumber(wardNumber);
		}
		//ends
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, uriVariables);
		log.info("URL--->" + url);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, roleWard, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}

	@Override
	public List<ResidentDTO> getSortedResidents(HttpServletRequest request, String token) {
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
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<ResidentDTO>>> responseType = new ParameterizedTypeReference<Response<List<ResidentDTO>>>() {
		};
		List<ResidentDTO> residents = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return residents;
	}

	@Override
	public String getTotalHouseCountByWard(String wardNo, String toleName, String token) {
		
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
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<Integer>> responseType = new ParameterizedTypeReference<Response<Integer>>() {
		};
		Integer totalHouseCount = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return totalHouseCount.toString();
	}


}
