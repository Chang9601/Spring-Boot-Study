package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록, IoC
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Transactional(readOnly = true)
	public User 회원찾기(String username) {
		User user = userRepository.findByUsername(username).orElseGet(() -> {
			return new User();
		});
		
		return user;
	}
	
	@Transactional
	public void 회원가입(User user) {
		String rawPassword = user.getPassword(); // 1234
		String encPassword = encoder.encode(rawPassword); // 해시
		
		user.setRole(RoleType.USER);
		user.setPassword(encPassword);
		userRepository.save(user);
	}
	
	@Transactional
	public void 회원수정(User user) {
		// 수정 시 영속성 컨텍스트 User 객체를 영속화시키고, 영속화된 객체를 수정
		// select를 해서 User 객체를 DB에서 가져오는 이유는 영속화를 하기 위해서
		// 영속화된 객체를 변경하면 자동으로 DB에 update문 전달
		User persistence = userRepository.findById(user.getId()).orElseThrow(() -> {
			return new IllegalArgumentException("글 상세보기 실패: 아이디 없음");
		});
		
		// Validation 확인: oAuth 값이 없으면 수정 가능
		if(persistence.getOAuth() == null || persistence.getOAuth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistence.setPassword(encPassword);	
			persistence.setEmail(user.getEmail());			
		}
		// 회원수정 함수 종료 시 = 서비스 종료 = 트랜잭션 종료 = commit
		// 영속화된 객체의 변화가 감지되면 더티체킹이 되어 update문 자동 전송
	}
}