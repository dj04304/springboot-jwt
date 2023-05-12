package com.jun.springsecurity1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

	@Override //여기서 후처리가 된다. 구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		System.out.println("getClientRegistration:" + userRequest.getClientRegistration());
//		System.out.println("getAccessToken:" + userRequest.getAccessToken().getTokenValue());
//		System.out.println("getAttributes:" +  super.loadUser(userRequest).getAttributes());
		
		//구글로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken요청
		//userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원 프로필을 받아준다.
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		//구글계정 회원가입을 강제로 진행해볼 예정
		return super.loadUser(userRequest);
	}
}