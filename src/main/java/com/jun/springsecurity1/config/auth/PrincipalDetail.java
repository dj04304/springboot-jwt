package com.jun.springsecurity1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jun.springsecurity1.model.User;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되면 시큐리티 session을 만들어 준다. (Security ContextHolder)
// 오브젝트 => Authentication 타입 객체
// Authentication 내에 User정보가 있어야 한다.
// User오브젝트 타입 => UserDetails 타입 객체여야 한다.

//정리: Security Session에 정보를 저장 => Authentication객체여야 한다. => Authentication 안에 user정보를 저장할 때는 UserDetails 여야 한다.
// Authentication 안에있는 UserDetails를 꺼내야 함
// PrincipalDetails === UserDetails

public class PrincipalDetail implements UserDetails{
	
	private User user; // 콤포지션
	
	public PrincipalDetail(User user) {
		this.user = user;
		
	}

	//해당 User의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		// 우리 사이트에서 1년동안 회원이 로그인하지 않으면 휴먼 계정으로 하기로 함.
		// 현재시간 = 로그인 시간 => 1년을 초과하면 return false;
		
		return true;
	}

}
