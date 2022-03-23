package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.CartItems;

public interface CartItemsRepo extends JpaRepository<CartItems, Integer> {

}
