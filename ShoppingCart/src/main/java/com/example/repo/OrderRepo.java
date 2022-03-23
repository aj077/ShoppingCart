package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Integer> {

}
