package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/**
// 그냥 주소가 / 이면 index.jsp 허용
// static 이하에 있는 /js/**, /css/**, /image/**
@Controller
public class UserController {
	
	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
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
	public String kakaoCallback(String code) { // 데이터를 반환하는 어노테이션
		
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
		params.add("client_id", clientId);
		params.add("redirect_uri", redirectId);
		params.add("code", code);
		
		// HttpHeader와 HttpBody를 하나의 객체로 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenReq = 
			new HttpEntity<>(params, headers);
		
		// Http 요청 - POST 방식, Response 변수의 응답
		ResponseEntity<String> resp = rt.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoTokenReq,
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
		
		RestTemplate rt2 = new RestTemplate();
		
		// HttpHeaders 객체 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpHeader와 HttpBody를 하나의 객체로 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileReq = 
			new HttpEntity<>(headers2);
		
		// Http 요청 - POST 방식, Response 변수의 응답
		ResponseEntity<String> resp2 = rt2.exchange(
			"https://kapi.kakao.com/v2/user/me",
			HttpMethod.POST,
			kakaoProfileReq,
			String.class
		);

		// Gson, JsonSimple, ObjectMapper
		ObjectMapper ob2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		
		try {
			kakaoProfile = ob2.readValue(resp2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} 
		
		// User 클래스: username, password, email
		System.out.println("카카오 아이디(번호): " + kakaoProfile.getId());
		System.out.println("카카오 이메일(번호): " + kakaoProfile.getKakao_account().getEmail());
		
		System.out.println("블로그 서버 username: " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
		System.out.println("블로그 서버 email: " + kakaoProfile.getKakao_account().getEmail());
		//UUID garbagePassword = UUID.randomUUID();
		//UUID: 중복되지 않는 값을 만들어내는 알고리즘
		System.out.println("블로그 서버 password: " + cosKey);
		
		User kakaoUser = User.builder()
			.username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
			.password(cosKey)
			.email(kakaoProfile.getKakao_account().getEmail())
			.oAuth("kakao")
			.build();
		
		// 가입자, 비가입자 확인 후 처리
		User user = userService.회원찾기(kakaoUser.getUsername());
		if(user.getUsername() == null) {
			System.out.println("기존 회원 X");

			userService.회원가입(kakaoUser);		
		}
		
		// 로그인 처리
		System.out.println("자동 로그인");		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/";
	}
}