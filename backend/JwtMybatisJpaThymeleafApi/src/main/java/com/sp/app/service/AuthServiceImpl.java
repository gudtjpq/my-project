package com.sp.app.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sp.app.domain.dto.KakaoTokenDto;
import com.sp.app.domain.dto.KakaoUserDto;
import com.sp.app.domain.dto.LoginUser;
import com.sp.app.domain.dto.MemberDto;
import com.sp.app.domain.dto.RefreshTokenDto;
import com.sp.app.domain.dto.TokenRequestDto;
import com.sp.app.mapper.MemberMapper;
import com.sp.app.security.JwtToken;
import com.sp.app.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
	private final MemberService memberService;
	private final MemberMapper memberMapper;
	private final JwtTokenProvider tokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${kakao.client-id}")
	private String clientId;
	
	@Value("${kakao.client-secret}")
	private String clientSecret;

	@Value("${kakao.redirect-uri}")
	private String redirectUri;

	@Value("${kakao.token-url}")
	private String tokenUrl;

	@Value("${kakao.user-info-url}")
	private String userInfoUrl;

	@Override
	public JwtToken login(String username, String password) throws Exception {
		MemberDto member = memberService.findById(username);
		if (member == null || !passwordEncoder.matches(password, member.getPassword())) {
			throw new Exception("Invalid login");
		}

		Authentication authentication = createAuthentication(member);
		JwtToken jwtToken = tokenProvider.generateToken(authentication);
		
		saveOrUpdateRefreshToken(member.getLogin_id(), jwtToken.getRefreshToken());
		memberService.updateLastLogin(member.getMember_id());

		return jwtToken;
	}

	@Override
	public JwtToken kakaoLogin(String code) throws Exception {
		String kakaoAccessToken = getKakaoAccessToken(code);
		KakaoUserDto kakaoUser = getKakaoUserInfo(kakaoAccessToken);

		MemberDto member = memberService.loginSnsMember(Map.of(
				"sns_provider", "kakao",
				"sns_id", String.valueOf(kakaoUser.getId())
		));

		if (member == null) {
			member = new MemberDto();
			member.setSns_provider("kakao");
			member.setSns_id(String.valueOf(kakaoUser.getId()));
			
			String nickname = "카카오유저";
			if (kakaoUser.getProperties() != null && kakaoUser.getProperties().getNickname() != null) {
				nickname = kakaoUser.getProperties().getNickname();
			} else if (kakaoUser.getKakao_account() != null && kakaoUser.getKakao_account().getProfile() != null) {
				nickname = kakaoUser.getKakao_account().getProfile().getNickname();
			}
			member.setName(nickname);
			
			String email = (kakaoUser.getKakao_account() != null) ? kakaoUser.getKakao_account().getEmail() : null;
			member.setEmail(email);
			
			memberService.insertSnsMember(member);
			
			member = memberService.loginSnsMember(Map.of(
					"sns_provider", "kakao",
					"sns_id", String.valueOf(kakaoUser.getId())
			));
		}

		Authentication authentication = createAuthentication(member);
		JwtToken jwtToken = tokenProvider.generateToken(authentication);
		
		saveOrUpdateRefreshToken(member.getLogin_id(), jwtToken.getRefreshToken());
		memberService.updateLastLogin(member.getMember_id());

		return jwtToken;
	}

	@Override
	public LoginUser getLoginUser(String accessToken) {
		Authentication authentication = tokenProvider.getAuthentication(accessToken);
		MemberDto member = memberService.findById(authentication.getName());
		
		if (member == null) return null;

		return LoginUser.builder()
				.member_id(member.getMember_id())
				.login_id(member.getLogin_id())
				.name(member.getName() != null ? member.getName() : "사용자")
				.role(member.getAuthority())
				.email(member.getEmail())
				.build();
	}

	@Override
	public JwtToken reissue(TokenRequestDto tokenRequestDto) throws Exception {
		if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
			throw new Exception("Refresh Token is invalid");
		}

		Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
		RefreshTokenDto refreshTokenDto = memberMapper.findByToken(authentication.getName());
		
		if (refreshTokenDto == null || !refreshTokenDto.getRt_value().equals(tokenRequestDto.getRefreshToken())) {
			throw new Exception("Refresh Token does not match");
		}

		JwtToken jwtToken = tokenProvider.generateToken(authentication);
		saveOrUpdateRefreshToken(authentication.getName(), jwtToken.getRefreshToken());

		return jwtToken;
	}

	@Override
	public MemberDto findById(String login_id) {
		return memberService.findById(login_id);
	}

	@Override
	public String findByAuthority(String login_id) {
		return memberService.findByAuthority(login_id);
	}

	private Authentication createAuthentication(MemberDto member) {
		if (member == null || member.getLogin_id() == null) {
			throw new RuntimeException("회원 정보가 올바르지 않습니다.");
		}
		
		String auth = (member.getAuthority() != null) ? member.getAuthority() : "USER";
		List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + auth));
		
		UserDetails userDetails = User.builder()
				.username(member.getLogin_id())
				.password("")
				.authorities(authorities)
				.build();
				
		return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
	}

	private void saveOrUpdateRefreshToken(String loginId, String rt) {
		RefreshTokenDto dto = memberMapper.findByToken(loginId);
		if (dto == null) {
			memberMapper.insertRefreshToken(new RefreshTokenDto(loginId, rt));
		} else {
			dto.setRt_value(rt);
			memberMapper.updateRefreshToken(dto);
		}
	}

	private String getKakaoAccessToken(String code) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", "authorization_code");
			params.add("client_id", clientId);
			params.add("client_secret", clientSecret);
			params.add("redirect_uri", redirectUri);
			params.add("code", code);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
			ResponseEntity<KakaoTokenDto> res = restTemplate.postForEntity(tokenUrl, request, KakaoTokenDto.class);
			return res.getBody().getAccess_token();
		} catch (Exception e) {
			log.error("카카오 토큰 발급 에러: ", e);
			throw e;
		}
	}

	private KakaoUserDto getKakaoUserInfo(String token) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			ResponseEntity<KakaoUserDto> res = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, KakaoUserDto.class);
			return res.getBody();
		} catch (Exception e) {
			log.error("카카오 사용자 정보 조회 에러: ", e);
			throw e;
		}
	}
}