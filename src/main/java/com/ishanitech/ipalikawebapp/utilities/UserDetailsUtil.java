package com.ishanitech.ipalikawebapp.utilities;

import com.ishanitech.ipalikawebapp.dto.UserDTO;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

public class UserDetailsUtil {
	
    public static UserDTO getLoggedInUser(Authentication auth) {
        return (UserDTO )auth.getPrincipal();
    }

    public static boolean isLoggedIn(Authentication auth) {
        return !(auth instanceof AnonymousAuthenticationToken);
    }
    
    public static String getToken(UserDTO user) {
    	return user.getToken();
    }
}
