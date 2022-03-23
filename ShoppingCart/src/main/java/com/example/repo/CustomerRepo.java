package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, String> {

	public Customer findByEmail(String email);
}
