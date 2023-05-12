package com.jun.springsecurity1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jun.springsecurity1.config.auth.PrincipalDetails;
import com.jun.springsecurity1.model.User;
import com.jun.springsecurity1.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	// Authentication 이라는 인터페이스를 통해 User정보를 가져올 수 도 있고, @AuthenticationPrincipal 라는 어노테이션을 통해서도 User 정보를 가져올 수 있다.
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal  PrincipalDetails PrincipalDetails) {
		System.out.println("/test/login ==================");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication: " + principalDetails.getUser());
		
		System.out.println("userDetails: " + PrincipalDetails.getUser());
		return "세션 정보 확인하기";
	}
	
	// PrincipalDetails 캐스팅하여 사용시 google정보는 가져오지 못한다. 그래서 OAuth2User에서 google정보를 가져오면 된다.
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) { //DI(의존성 주입)
		System.out.println("/test/oauth/login ==================");
		
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication: " + oauth2User.getAttributes());
		System.out.println("oauth2User: " + oauth.getAttributes());
		
		return "OAuth 세션 정보 확인하기";
	}
	
	@GetMapping({"","/"})
	public String index() {
		//mustache => 기본폴더: src/main/resources/
		//viewResolver 설정: templates (prefix), .mustache (suffix) 생략 가능!
		return "index"; //src/main/resources/templates/index.mustache
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	//스프링 시큐리티가 해당 주소를 낚아채버림 => SecurityConfig 파일 생성 후 작동안함
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		userRepository.save(user); // 회원가입 잘되긴 한다. 그러나 비밀번호가 1234 이기 때문에 시큐리티로 로그인 할 수 없음(패스워드가 암호화가 안됨)
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN") // 이런식으로 페이지에 권한을 줄 수 있다.
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // Secured와 다르게 여러개를 걸 수 있음
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터 정보";
	}
}
