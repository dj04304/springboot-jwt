package com.jun.springsecurity1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jun.springsecurity1.model.User;

// CRUD 함수를 JpaRepository가 들고있음
// @Repository라는 어노테이션이 없어도 IoC가 가능하다. 이유는 JpaRepository를 상속받았기 때문
public interface UserRepository extends JpaRepository<User, Integer>{
	// findBy까지는 규칙 -> Username은 문법
	// Select * from user where username = 1?(username) 이 호출됨
	public User findByUsername(String username); //Jpa Query method 검색해서 공부해보기 
	
//	// Select * from user where email = ?
//	public User findByEmail();
}
