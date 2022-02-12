package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob
	private String content; // 썸머노트 라이브러리 <html> 태그가 섞여서 디자인
	
	@ColumnDefault("0")
	private int count; // 조회수
	
	@ManyToOne // Many = Board, One = User
	@JoinColumn(name = "userId")
	private User user; // DB는 객체 저장 불가능. FK, 자바는 저장 가능
	
	@CreationTimestamp // 시간이 자동입력
	private Timestamp createDate;	
}