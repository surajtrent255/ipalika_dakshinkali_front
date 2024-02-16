/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Feb 14, 2020
 */
package com.ishanitech.ipalikawebapp.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "rest-api")
public class RestApiProperties {
	private String protocol;
	private String domain;
	private int port;
}
