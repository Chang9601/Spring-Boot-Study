package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.User;

// DAO
// 자동으로 Bean 등록
// @Repository 생략 가능
public interface UserRepository extends JpaRepository<User, Integer>{
	
}