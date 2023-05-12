package com.jun.springsecurity1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jun.springsecurity1.config.oauth.PrincipalOauth2UserService;

/**
 * 
 * 
 * 
 * 1. 코드 받기(인증)  
 * 2. 액세스 토큰(권한) 
 * 3.사용자 프로필 정보를 가져오고
 * 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시킴
 * 4-2 (이메일, 전화번호, 이름, 아이디) 예를들어 쇼핑몰 -> (집주소), 백화점몰 -> (vip등급, 일반등급) 필요
 */

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //Secured 어노테이션 활성화, PreAuthorize, PostAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService; 
	
	// 해당 메서드의 리턴되는 로브젝으를 IoC로 등록해준다.
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
				.antMatchers("/user/**").authenticated() //인증만 되면 들어갈 수 있는 주소
				.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
				.anyRequest().permitAll()
				
				.and()
				
				.formLogin()
				.loginPage("/loginForm")
				.loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
				.defaultSuccessUrl("/")
		
				.and()
				
				.oauth2Login()
				.loginPage("/loginForm")
				.userInfoEndpoint()
				.userService(principalOauth2UserService);  // 구글 로그인이 완료된 이후의 후처리가 필요함. TIp. 코드안받음(엑세스 토큰 + 사용자 프로필 정보를 받음)
	}
	
}
