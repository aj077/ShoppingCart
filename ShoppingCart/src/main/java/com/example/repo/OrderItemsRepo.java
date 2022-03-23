package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.OrderItems;

public interface OrderItemsRepo extends JpaRepository<OrderItems, Integer> {

}
