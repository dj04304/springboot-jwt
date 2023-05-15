package com.jun.springsecurity1.model;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue
	private int id;
	private String username;
	private String password;
	private String email;
	private String role;
	
	private String provider;
	private String providerId;
	@CreationTimestamp
	private Timestamp CreateDate;
	
	

	
}
