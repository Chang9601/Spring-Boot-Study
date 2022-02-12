package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ORM: 객체 -> 테이블 매핑하는 기술
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // User 클래스가 MySQL에 테이블이 생성
public class User {

	@Id // Primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id; // Oracle: 시퀀스, MySQL: auto_increment
	
	@Column(nullable = false, length = 30)
	private String username;
	
	@Column(nullable = false, length = 100)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	
	@ColumnDefault("'user'")
	private String role; // Enum 사용 권고: admin, user, manager
	
	@CreationTimestamp // 시간이 자동입력
	private Timestamp createDate;	
}