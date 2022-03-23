package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Comments;

public interface CommentsRepo extends JpaRepository<Comments, Integer> {

}
