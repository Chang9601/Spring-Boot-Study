package com.cos.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Bean 등록: 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것

@Configuration // Bean 등록 (IoC 관리)
@EnableWebSecurity // 시큐리티 필터가 등록이 된다.
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 확인
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean // Bean 등록 (IoC 관리)
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // csrf 토큰 비활성화 (테스트 시 사용)
			.authorizeHttpRequests()
				.antMatchers("/", "/auth/**" ,"/js/**", "/css/**", "/image/**") // 다음 요청은 누구나 접속 가능
				.permitAll()
				.anyRequest() // 다른 모든 요청 인증 필요
				.authenticated()
			.and()
				.formLogin()
				.loginPage("/auth/loginForm"); 
	}
}