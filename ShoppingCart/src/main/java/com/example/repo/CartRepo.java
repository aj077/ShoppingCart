package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Cart;

public interface CartRepo extends JpaRepository<Cart, Integer> {

}
