package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

// HTML 파일이 아니라 데이터를 반환하는 controller = ResteController
@RestController
public class DummyControllerTest {
	
	@Autowired // 의존성 주입(DI) 
	private UserRepository userRepository;
	
	
	// save 함수는 ID를 전달 X -> INSERT
	// save 함수는 ID를 전달 O -> 데이터 O -> UPDATE
	// save 함수는 ID를 전달 O -> 데이터 X -> INSERT
	// email, password
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제 실패. 해당 id 존재 X";
		}
		
		return "삭제 완료. id: " + id;
	}
	
	
	@Transactional // 함수 종료 시 자동 commit 
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) { // JSON 데이터 요청: Java Object(MessageConverter의 Jackson 라이브러리가 변환)
		System.out.println("id:" + id);
		System.out.println("password:" + requestUser.getPassword());
		System.out.println("email:" + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("수정 실패");
		});
		
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		//userRepository.save(user);
		
		// 더티 체킹
		return user;
	}
	
	// http://localhost:8000/blog/dummy/user	
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	// 한 페이지 당 2개의 데이터 바환
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<User> pagingUser = userRepository.findAll(pageable);
		 	
		List<User> users = pagingUser.getContent();
		
		return users;
	}
		
	// {id} 주소로 파라미터를 전달 받을 수 있다.
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// user/4 찾을 경우, 데이터베이스 못 찾을 경우 user가 null
		// 따라서 프로그램 오류 발생
		// Optional로 user 객체를 감싸서 가져온다. 개발자가 null인지 아닌지 판단해서 반환
		
		//User user = userRepository.findById(id).orElseThrow(() -> {
		//	return new IllegalArgumentException("해당 유저 존재 X");
		//});
		
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 사용자 아이디 없음, id: " + id);
			}
			
		});
		
		// 요청: 웹 브라우저
		// user 객체: 자바 객체 
		// 변환: 웹 브라우저가 이해할 수 있는 데이터 -> JSON(e.g. Gson 라이브러리)
		// 스프링 부트: MessageConverter 응답 시 자동 작동
		// 자바 객체를 반환하면 MessageConverter가 Jackson 라이브러리 호출
		// user 객체를 JSON으로 변환해서 브라우저에게 전달
		return user;
	}
	
	// http://localhost:8000/blog/dummy/join (요청)
	// http의 body에 username, password, email 데이터를 가지고 요청
	@PostMapping("/dummy/join")
	public String join(User user) { // key=value(약속된 규칙)
		System.out.println("id: " + user.getId());
		System.out.println("username: " + user.getUsername());
		System.out.println("password: " + user.getPassword());
		System.out.println("email: " + user.getEmail());
		System.out.println("role: " + user.getRole());
		System.out.println("createDate: " + user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입 완료";
	}
}
