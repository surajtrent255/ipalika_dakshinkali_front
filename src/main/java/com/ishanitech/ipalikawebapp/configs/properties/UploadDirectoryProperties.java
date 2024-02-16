package com.ishanitech.ipalikawebapp.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "com")
public class UploadDirectoryProperties {

	private String tempFileUploadingDirectory;
}
