package com.ishanitech.ipalikawebapp.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ishanitech.ipalikawebapp.dto.UserDTO;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		handleRequest(request, response, authentication);
		clearAuthenticationAttributes(request);
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}

		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	private void handleRequest(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		String targetUrl = determineTargetUrl(authentication);
		if (response.isCommitted()) {
			return;
		}
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	private String determineTargetUrl(Authentication authentication) {
		boolean isSuperAdmin = false, isCentralAdmin = false, isWardAdmin = false, isSurveyor = false, isHouseHold = false;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ROLE_SUPER_ADMIN")) {
				isSuperAdmin = true;
				break;

			} else if (authority.getAuthority().equals("ROLE_CENTRAL_ADMIN")) {
				isCentralAdmin = true;
				break;
			} else if(authority.getAuthority().equals("ROLE_WARD_ADMIN")) {
				isWardAdmin = true;
				break;
			} else if (authority.getAuthority().equals("ROLE_HOUSEHOLD")){
				isHouseHold = true;
				return "/household/"+ ((UserDTO)authentication.getPrincipal()).getFilledId();
			}	
			
			else {
				isSurveyor = true;
				return "/resident";
			}
		}
//		if (isSuperAdmin) {
//			return "/super-admin/";
//		} else if (isCentralAdmin) {
//			return "/central-admin";
//		} else if(isWardAdmin) {
//			return "/ward-admin";
//		} else {
//			return "/surveyor";
//		}
		return "/report";
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}
}
