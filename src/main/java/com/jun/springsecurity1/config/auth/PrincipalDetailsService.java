package com.jun.springsecurity1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jun.springsecurity1.model.User;
import com.jun.springsecurity1.repository.UserRepository;


// security 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	
	//파라미터 username 은 loginForm의 로그인 input태그 name의 username에서 받는것이기 때문에 loginForm에서 username을 잘 써줘야 한다.
	//파라미터를 바꾸고싶으면 Security Config builder에서 바꾸면 되지만 그냥 username쓰자
	
	// Security session(내부 Authentication(내부 UserDetails))
	// @AuthenicationPrincipal 어노테이션이 만들어진다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username" + username);
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		
		return null;
	}

}
