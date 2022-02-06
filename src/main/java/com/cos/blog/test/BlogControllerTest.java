package com.cos.blog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 스프링이 com.cos.blog 패키지 이하를 스캔해서 특정 어노테이션을 가지는 클래스 파일들을 new(IoC)를 해서 스프링 컨테이너가 관리
@RestController
public class BlogControllerTest {
	// http://localhost:8080/test/hello
	@GetMapping("/test/hello")
	public String hello() {
		return "<h1>Hello Spring Boot</h1>";
	}
}