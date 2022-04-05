package com.cos.blog.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/**
// 그냥 주소가 / 이면 index.jsp 허용
// static 이하에 있는 /js/**, /css/**, /image/**
@Controller
public class UserController {
	
	private final String clientId = "";
	private final String redirectId = "";
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		
		return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		
		return "user/loginForm";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm() {
		
		return "user/updateForm";
	}
	
	@GetMapping("/auth/kakao/callback")
	public @ResponseBody String kakaoCallback(String code) { // 데이터를 반환하는 어노테이션
		
		// 카카오로 POST 방식, key=value 데이터 요청
		// Retrofit2
		// OkHttp
		// RestTemplate
		
		RestTemplate rt = new RestTemplate();
		
		// HttpHeaders 객체 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpBody 객체 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "");
		params.add("redirect_uri", "");
		params.add("code", code);
		
		// HttpHeader와 HttpBody를 하나의 객체로 담기
		HttpEntity<MultiValueMap<String, String>> kakaoReq = 
			new HttpEntity<>(params, headers);
		
		// Http 요청 - POST 방식, Response 변수의 응답
		ResponseEntity<String> resp = rt.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoReq,
			String.class
		);
		
		// Gson, JsonSimple, ObjectMapper
		ObjectMapper ob = new ObjectMapper();
		OAuthToken oAuthToken = null;
		
		try {
			oAuthToken = ob.readValue(resp.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} 
		
		System.out.println("카카오 AccessToken: " + oAuthToken.getAccess_token());
		
		return resp.getBody();
	}
}