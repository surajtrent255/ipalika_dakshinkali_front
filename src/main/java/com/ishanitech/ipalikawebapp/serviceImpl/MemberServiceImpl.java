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
import com.ishanitech.ipalikawebapp.dto.FamilyMemberDTO;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.RoleWardDTO;
import com.ishanitech.ipalikawebapp.service.MemberService;
import com.ishanitech.ipalikawebapp.utilities.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

	private final String MEMBER_BASE_URL = "member";
	private final RestTemplate restTemplate;
	private final RestApiProperties restApiProperties;
	
	public MemberServiceImpl(RestTemplate restTemplate, RestApiProperties restApiProperties) {
		this.restTemplate = restTemplate;
		this.restApiProperties = restApiProperties;
	}
	
	
	@Override
	public Response<?> getMemberDataList(String token, List<String> roles, int wardNumber, HttpServletRequest httpServletRequest) {
		
		RoleWardDTO roleWard = new RoleWardDTO();
		if(roles.contains("WARD_ADMIN")) {
			roleWard.setRole(3);
			roleWard.setWardNumber(wardNumber);
		}
		Map<String , String[]> maps = httpServletRequest.getParameterMap();


//		if(httpServletRequest.getParameter("prevLastItemId") != null && !httpServletRequest.getParameter("searchKey").trim().isEmpty()){
//			System.out.println("hello");
//			roleWard.getUrlObject().setPrevLastItemId(Integer.parseInt(httpServletRequest.getParameter("prevLastItemId")));
//		}
		Integer prevLastItemId = httpServletRequest.getParameter("prevLastItemId") != null ?
				Integer.parseInt(httpServletRequest.getParameter("prevLastItemId")) : 0;
		String searchKey = httpServletRequest.getParameter("searchKey") != null ? httpServletRequest.getParameter("searchKey"):"";
		Integer wardNo = httpServletRequest.getParameter("wardNo") != null ? Integer.parseInt(httpServletRequest.getParameter("wardNo")) : 0;
		Integer currentPage = httpServletRequest.getParameter("currentPage") != null ? Integer.parseInt(httpServletRequest.getParameter("currentPage")):0;;
		Integer pageLimit = httpServletRequest.getParameter("pageLimit") != null ? Integer.parseInt(httpServletRequest.getParameter("pageLimit")):0;;
		String backFrom = httpServletRequest.getParameter("backFrom") != null ? httpServletRequest.getParameter("backFrom"): "";;


		RoleWardDTO.UrlObject urlObject = new RoleWardDTO.UrlObject();
		urlObject.setPrevLastItemId(prevLastItemId);
		urlObject.setSearchKey(searchKey);
		urlObject.setWardNo(wardNo);
		urlObject.setCurrentPage(currentPage);
		urlObject.setPageLimit(pageLimit);
		urlObject.setBackFrom(backFrom);
		urlObject.setGoBack(true);
		roleWard.setUrlObject(urlObject);

		String template = String.format("%s", MEMBER_BASE_URL);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, roleWard, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<FamilyMemberDTO>>> responseType = new ParameterizedTypeReference<Response<List<FamilyMemberDTO>>>() {}; 
		Response<List<FamilyMemberDTO>> members = restTemplate.exchange(requestEntity, responseType).getBody();
		return members;
	}


	@Override
	public List<FamilyMemberDTO> searchMemberByKey(HttpServletRequest request, String searchKey, String wardNo,
			String token) {
		String template = String.format("%s/search", MEMBER_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("queryParamName", "searchKey");
		uriVariables.put("keyword", searchKey);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		String url = HttpUtils.createRequestUrlWithQueryString(restApiProperties, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<FamilyMemberDTO>>> responseType = new ParameterizedTypeReference<Response<List<FamilyMemberDTO>>>() {
		};
		List<FamilyMemberDTO> members = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return members;
	}


	@Override
	public List<FamilyMemberDTO> getMemberByWard(HttpServletRequest request, String wardNo, String token) {
		String template = String.format("%s/search/ward", MEMBER_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		String url = HttpUtils.createRequestUrlWithWardString(restApiProperties, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<FamilyMemberDTO>>> responseType = new ParameterizedTypeReference<Response<List<FamilyMemberDTO>>>() {
		};
		List<FamilyMemberDTO> members = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return members;
	}


	@Override
	public List<FamilyMemberDTO> getMemberByPageLimit(HttpServletRequest request, String wardNo, String token) {
		String template = String.format("%s/pageLimit", MEMBER_BASE_URL);
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		uriVariables.put("wardNo", wardNo);
		uriVariables.put("pageSize", request.getParameter("pageSize"));
		uriVariables.put("searchKey", request.getParameter("searchKey"));
		uriVariables.put("lastSeenId", request.getParameter("lastSeenId"));
		uriVariables.put("sortBy", request.getParameter("sortBy"));
		uriVariables.put("sortByOrder", request.getParameter("sortByOrder"));
		
		String url = HttpUtils.createRequestUrlWithPageLimit(restApiProperties, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<FamilyMemberDTO>>> responseType = new ParameterizedTypeReference<Response<List<FamilyMemberDTO>>>() {
		};
		List<FamilyMemberDTO> members = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return members;
	}


	@Override
	public List<FamilyMemberDTO> getNextLotMembers(HttpServletRequest request, List<String> roles, int wardNo,
			String token) {
		
		String endPoint = "?";
		if(request.getParameter("searchKey") != null) {
			endPoint += "searchKey=" + request.getParameter("searchKey") + "&";
		}
		if(request.getParameter("wardNo") != null) {
			endPoint += "wardNo=" + request.getParameter("wardNo") + "&";
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
		String template = String.format("%s/nextLot" + endPoint, MEMBER_BASE_URL);
		
		RoleWardDTO roleWard = new RoleWardDTO();
		if(roles.contains("WARD_ADMIN")) {
			roleWard.setRole(3);
			roleWard.setWardNumber(wardNo);
		}
		
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, roleWard, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<FamilyMemberDTO>>> responseType = new ParameterizedTypeReference<Response<List<FamilyMemberDTO>>>() {
		};
		List<FamilyMemberDTO> members = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return members;
	}


	@Override
	public List<FamilyMemberDTO> getSortedMembers(HttpServletRequest request, String token) {
		String endPoint = "?";
		if(request.getParameter("searchKey") != null) {
			endPoint += "searchKey=" + request.getParameter("searchKey") + "&";
		}
		if(request.getParameter("wardNo") != null) {
			endPoint += "wardNo=" + request.getParameter("wardNo") + "&";
		}
		//endPoint += "last_seen=" + request.getParameter("lastSeenId") + "&";
		//endPoint += "action=" + request.getParameter("action") + "&";
		endPoint += "sortBy=" + request.getParameter("sortBy") + "&";
		endPoint += "sortByOrder=" + request.getParameter("sortByOrder") + "&";
		endPoint += "pageSize=" + request.getParameter("pageLimit");
		
		String template = String.format("%s/sortBy" + endPoint, MEMBER_BASE_URL);
		
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("rootAddress", template);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, uriVariables);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<Response<List<FamilyMemberDTO>>> responseType = new ParameterizedTypeReference<Response<List<FamilyMemberDTO>>>() {
		};
		List<FamilyMemberDTO> members = restTemplate.exchange(requestEntity, responseType).getBody().getData();
		return members;
	}

}
