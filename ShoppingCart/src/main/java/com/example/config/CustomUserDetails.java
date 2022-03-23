package com.example.config;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.entity.Customer;

public class CustomUserDetails implements UserDetails {
	
	Customer cust;	

	public CustomUserDetails(Customer cust) {
		super();
		this.cust = cust;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		HashSet<SimpleGrantedAuthority> mySet = new HashSet<>();
		mySet.add(new SimpleGrantedAuthority(cust.getRole()));
		return mySet;
	}

	@Override
	public String getPassword() {
		
		return cust.getPassword();
	}

	@Override
	public String getUsername() {
		
		return cust.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

}
