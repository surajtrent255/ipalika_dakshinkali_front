package com.ishanitech.ipalikawebapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Contains the information for new user registration. 
 * @author <b> Umesh Bhujel
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class UserRegistrationDTO {
	private String fullName;
	private String username;
	private String email;
	private String password;
	private String mobileNumber;
	private int accountType;
	private int wardNo;
}
