package com.example.config;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		CustomUserDetails customerDetails = (CustomUserDetails)authentication.getPrincipal();
		
		HashSet<SimpleGrantedAuthority> authorities = (HashSet<SimpleGrantedAuthority>) customerDetails.getAuthorities();
		
		String role = null;
		String redirectUrl = request.getContextPath();
		
		System.out.println("ContextPath: "+redirectUrl);
		
		for(SimpleGrantedAuthority auth : authorities) {
			if(auth.getAuthority().equals("ROLE_ADMIN")) {
				redirectUrl += "/admin/home";
			}
		}
		response.sendRedirect(redirectUrl);
	}

}
