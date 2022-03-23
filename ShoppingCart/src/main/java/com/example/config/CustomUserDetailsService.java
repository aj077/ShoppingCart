package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entity.Customer;
import com.example.repo.CustomerRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	CustomerRepo cRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Customer cust = cRepo.findByEmail(email);
		if(cust == null) {
			throw new UsernameNotFoundException("Customer not found");
		}
		return new CustomUserDetails(cust);
	}

}
