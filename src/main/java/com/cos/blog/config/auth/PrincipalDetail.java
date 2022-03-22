package com.cos.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.blog.model.User;

import lombok.Getter;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 객체를
// 스프링 시큐리티의 고유한 세션저장소에 저장을 한다.
@Getter
public class PrincipalDetail implements UserDetails {
	private User user; // 컴포지션
	
	
	public PrincipalDetail(User user) {
		this.user = user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}
	
	// 계정이 만료되지 않았는지 반환 (true: 만료 X)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정이 잠겨있지 않았는지 반환 (true: 잠김 X)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호가 만료되지 않았는지 반환 (true: 만료 X)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계졍이 활성화(사용 가능)인지 반환 (true: 활성화)
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// 계정이 갖고있는 권한 목록 반환 (권한이 여러 개 있을 수 있어서 루프를 돌아야 하는데 지금은 한 개만)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		collectors.add(() -> {
			return "ROLE_" + user.getRole();
		});
		
		return collectors;
	}
}