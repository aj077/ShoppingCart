package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

}
