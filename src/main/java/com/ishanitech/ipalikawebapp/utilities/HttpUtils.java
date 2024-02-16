package com.ishanitech.ipalikawebapp.utilities;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.ishanitech.ipalikawebapp.configs.properties.RestApiProperties;

public class HttpUtils {
	public static HttpHeaders createHeader(@Nullable MediaType mediaType, @Nullable String token) {
		HttpHeaders headers = new HttpHeaders();
		if(mediaType != null) {
			headers.setContentType(mediaType);
		}
		if(token != null) {
			headers.set("Authorization", token);
		}
		return headers;
	}
	
	public static <T> RequestEntity<T> createRequestEntity(HttpMethod method,
			T entity, MediaType mediaType,
			@Nullable String token, String url) {
			url = url.replace(" ", "spacex");
			RequestEntity<T> requestEntity = null;
			try {
				requestEntity =  RequestEntity
							.method(method, new URI(url))
							.contentType(mediaType)
							.header("Authorization", token)
							.body(entity);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return requestEntity;
	}
	
	public static RequestEntity createRequestEntity(HttpMethod method,
			MediaType mediaType, @Nullable String token, String url) {
		RequestEntity requestEntity = null;
		try {
			requestEntity = RequestEntity
					.method(method, new URI(url))
					.contentType(mediaType)
					.header("Authorization", token)
					.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return requestEntity;
	}
	
	public static RequestEntity createRequestEntity(HttpMethod method,
			MediaType mediaType, String url) {
		RequestEntity requestEntity = null;
		try {
			requestEntity = RequestEntity
					.method(method, new URI(url))
					.contentType(mediaType)
					.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return requestEntity;
	}
	
	
	public static String createRequestUrl(RestApiProperties restApiProperties, String template, Map<String, Object> urlValues) {
		UriComponents uriComponent = UriComponentsBuilder.fromUriString(template)
				.scheme(restApiProperties.getProtocol())
				.host(restApiProperties.getDomain())
				.port(restApiProperties.getPort())
				.path("/")
				.build();
		if(urlValues != null) {
			uriComponent = uriComponent.expand(urlValues);
		}
		
		return uriComponent.toUriString();
	}
	
	public static String createRequestUrlWithQueryString(RestApiProperties restApiProperties, Map<String, Object> uriVariables) {
		String tolezNamez = "";
		if(uriVariables.get("toleName") == null) {
			tolezNamez = "";
		}else {
			tolezNamez = "&toleName=" + uriVariables.get("toleName");
		}
		UriComponents uriComponent = UriComponentsBuilder.newInstance()
				.scheme(restApiProperties.getProtocol())
				.host(restApiProperties.getDomain())
				.port(restApiProperties.getPort())
				.path("/{rootAddress}")
				.query("{queryParamName}={keyword}" + "&wardNo={wardNo}"+ tolezNamez  + "&pageSize={pageSize}").buildAndExpand(uriVariables).encode();
		return uriComponent.toUriString();
	}
	
	public static String createRequestUrlWithWardString(RestApiProperties restApiProperties, Map<String, Object> uriVariables) {
		UriComponents uriComponent = UriComponentsBuilder.newInstance()
				.scheme(restApiProperties.getProtocol())
				.host(restApiProperties.getDomain())
				.port(restApiProperties.getPort())
				.path("/{rootAddress}")
				.query("wardNo={wardNo}" + "&pageSize={pageSize}" + "&sortBy={sortBy}" + "&sortByOrder={sortByOrder}").buildAndExpand(uriVariables).encode();
		return uriComponent.toUriString();
	}
	
	public static String createRequestUrlWithToleString(RestApiProperties restApiProperties, Map<String, Object> uriVariables) {
		UriComponents uriComponent = UriComponentsBuilder.newInstance()
				.scheme(restApiProperties.getProtocol())
				.host(restApiProperties.getDomain())
				.port(restApiProperties.getPort())
				.path("/{rootAddress}")
				.query("wardNo={wardNo}" + "&toleName={toleName}" + "&pageSize={pageSize}" + "&sortBy={sortBy}" + "&sortByOrder={sortByOrder}").buildAndExpand(uriVariables).encode();
		return uriComponent.toUriString();
	}

	public static String createRequestUrlWithPageLimit(RestApiProperties restApiProperties,
			Map<String, Object> uriVariables) {
		
		String tolezNamez = "";
		if(uriVariables.get("toleName") == null) {
			tolezNamez = "";
		}else {
			tolezNamez = "&toleName=" + uriVariables.get("toleName");
		}
		UriComponents uriComponent = UriComponentsBuilder.newInstance()
				.scheme(restApiProperties.getProtocol())
				.host(restApiProperties.getDomain())
				.port(restApiProperties.getPort())
				.path("/{rootAddress}")
				.query("wardNo={wardNo}" + tolezNamez + "&pageSize={pageSize}" + "&searchKey={searchKey}" + "&lastSeenId={lastSeenId}" + "&sortBy={sortBy}" + "&sortByOrder={sortByOrder}").buildAndExpand(uriVariables).encode();
		return uriComponent.toUriString();
	}
	
	
	public static String createRequestUrlWithQueryStringFavPlace(RestApiProperties restApiProperties, Map<String, Object> uriVariables) {
		UriComponents uriComponent = UriComponentsBuilder.newInstance()
				.scheme(restApiProperties.getProtocol())
				.host(restApiProperties.getDomain())
				.port(restApiProperties.getPort())
				.path("/{rootAddress}")
				.query("{queryParamName}={keyword}" + "&wardNo={wardNo}"+ "&placeType={placeType}" + "&surveyor={surveyor}" + "&pageSize={pageSize}").buildAndExpand(uriVariables).encode();
		return uriComponent.toUriString();
	}
	
	public static String createRequestUrlWithWardStringFavPlace(RestApiProperties restApiProperties, Map<String, Object> uriVariables) {
		UriComponents uriComponent = UriComponentsBuilder.newInstance()
				.scheme(restApiProperties.getProtocol())
				.host(restApiProperties.getDomain())
				.port(restApiProperties.getPort())
				.path("/{rootAddress}")
				.query("wardNo={wardNo}" + "&placeType={placeType}" + "&surveyor={surveyor}" + "&pageSize={pageSize}" + "&sortBy={sortBy}" + "&sortByOrder={sortByOrder}").buildAndExpand(uriVariables).encode();
		return uriComponent.toUriString();
	}
	
	
	public static String createRequestUrlWithPageLimitFavPlace(RestApiProperties restApiProperties,
			Map<String, Object> uriVariables) {
		UriComponents uriComponent = UriComponentsBuilder.newInstance()
				.scheme(restApiProperties.getProtocol())
				.host(restApiProperties.getDomain())
				.port(restApiProperties.getPort())
				.path("/{rootAddress}")
				.query("wardNo={wardNo}" + "&placeType={placeType}" + "&surveyor={surveyor}" + "&pageSize={pageSize}" + "&searchKey={searchKey}" + "&lastSeenId={lastSeenId}" + "&sortBy={sortBy}" + "&sortByOrder={sortByOrder}").buildAndExpand(uriVariables).encode();
		return uriComponent.toUriString();
	}
}
