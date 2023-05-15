package com.jun.springsecurity1.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jun.springsecurity1.config.auth.PrincipalDetails;
import com.jun.springsecurity1.config.oauth.provider.FacebookUserInfo;
import com.jun.springsecurity1.config.oauth.provider.GoogleUserInfo;
import com.jun.springsecurity1.config.oauth.provider.NaverUserInfo;
import com.jun.springsecurity1.config.oauth.provider.OAuth2UserInfo;
import com.jun.springsecurity1.model.User;
import com.jun.springsecurity1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	// 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	@Override //여기서 후처리가 된다. 구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		System.out.println("getClientRegistration:" + userRequest.getClientRegistration());
//		System.out.println("getAccessToken:" + userRequest.getAccessToken().getTokenValue());
//		System.out.println("getAttributes:" +  super.loadUser(userRequest).getAttributes());
		
		//구글로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken요청
		//userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원 프로필을 받아준다.
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		OAuth2UserInfo oAuth2UserInfo = null;
		
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			// response = {response={id = ~, email = ~@naver.com, name = ~}`} 이런식으로 되어있기 때문에 .get("response") 해주는 것이다. 
			oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
		}else {
			System.out.println("구글, 페이스북, 네이버만 지원합니다.");
		}
		
		String provider = oAuth2UserInfo.getProvider();
		String providerId =oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId; // google_providerId => 충돌날 일이 없음
		String password = bCryptPasswordEncoder.encode("겟인데어");
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			
			userRepository.save(userEntity);
		}
		
		//구글계정 회원가입을 강제로 진행해볼 예정
		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}
}
