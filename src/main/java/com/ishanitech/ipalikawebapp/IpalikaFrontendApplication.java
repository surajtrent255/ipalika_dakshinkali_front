package com.ishanitech.ipalikawebapp;

import java.util.Arrays;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class IpalikaFrontendApplication {
	
	@Value("${server.port}")
	private int httpPort;

	public static void main(String[] args) {
		SpringApplication.run(IpalikaFrontendApplication.class, args);
	}
	
	/**
	 * Create bean for rest template. Using rest template we can call remote rest api.
	 * @return restTemplate RestTemplate object
	 */
    @Bean
    RestTemplate restTemplate() {
    	RestTemplate restTemplate = defaultRestTemplate();
    	restTemplate.setRequestFactory(clientHttpRequestFactory());
    	return restTemplate;
    }
    
    /**
	 * @return restTemplate a default rest template with default mapper.
	 */
	private static RestTemplate defaultRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		/*
		 * ObjectMapper objectMapper = new ObjectMapper();
		 * objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
		 * false); MappingJackson2HttpMessageConverter converter = new
		 * MappingJackson2HttpMessageConverter();
		 * converter.setObjectMapper(objectMapper);
		 * restTemplate.setMessageConverters(Arrays.asList(converter));
		 */
		return restTemplate;
	}

	/**
	 * 
	 * @return ClientHttpRequestFacoty custom http client request factory for unsupported http methods like patch, head etc.
	 */
	private ClientHttpRequestFactory clientHttpRequestFactory() {
    	HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    	factory.setConnectTimeout(30000);
    	factory.setReadTimeout(5000);
    	return factory;
    }
	
	
	// Lets configure additional connector to enable support for both HTTP and HTTPS
//	 	@Bean
//	 	@Profile("prod")
//	 	public ServletWebServerFactory servletContainer() {
//	 		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {@Override
//			protected void postProcessContext(Context context) {
//				SecurityConstraint securityConstraint = new SecurityConstraint();
//				securityConstraint.setUserConstraint("CONFIDENTIAL");
//				SecurityCollection collection = new SecurityCollection();
//				collection.addPattern("/*");
//				securityConstraint.addCollection(collection);
//				context.addConstraint(securityConstraint);
//			}
//		};
//	 		tomcat.addAdditionalTomcatConnectors(createStandardConnector());
//	 		return tomcat;
//	 	}

	 	private Connector createStandardConnector() {
	 		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	 		connector.setScheme("http");
	 		connector.setPort(httpPort);
	 		connector.setSecure(false);
	 		connector.setRedirectPort(443);
	 		return connector;
	 	}
	

}
