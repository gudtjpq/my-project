package com.sp.app.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sp.app.domain.dto.LoginUser;
import com.sp.app.domain.dto.TokenRequestDto;
import com.sp.app.security.JwtToken;
import com.sp.app.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth/*")
@Slf4j
public class AuthRestController {
	private final AuthService authService;

	@PostMapping("login")
	public ResponseEntity<?> signin(@RequestBody Map<String, String> requestMap) {
		try {
			String username = requestMap.get("login_id");
			String password = requestMap.get("password");
			
			JwtToken jwtToken = authService.login(username, password);

			LoginUser loginUser = authService.getLoginUser(jwtToken.getAccessToken());
			
			return ResponseEntity.ok(Map.of(
	                "accessToken", jwtToken.getAccessToken(), 
	                "refreshToken", jwtToken.getRefreshToken(),
	                "role", loginUser.getRole(),
	                "member_id", loginUser.getMember_id(),
	                "name", loginUser.getName()
	            ));
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 올바르지 않습니다.");
		}
	}
	
	@PostMapping("kakao")
	public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> requestMap) {
		try {
			String code = requestMap.get("code");
			
			JwtToken jwtToken = authService.kakaoLogin(code);
			
			LoginUser loginUser = authService.getLoginUser(jwtToken.getAccessToken());
			
			return ResponseEntity.ok(Map.of(
	                "accessToken", jwtToken.getAccessToken(), 
	                "refreshToken", jwtToken.getRefreshToken(),
	                "role", loginUser.getRole(),
	                "member_id", loginUser.getMember_id(),
	                "name", loginUser.getName()
	            ));
			
		} catch (Exception e) {
			log.error("카카오 로그인 백엔드 에러 발생: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카카오 로그인 처리 중 오류가 발생했습니다.");
		}
	}

	@PostMapping("refresh")
	public ResponseEntity<?> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
		try {
			return ResponseEntity.ok(authService.reissue(tokenRequestDto));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("세션이 만료되었습니다. 다시 로그인하세요.");
		}
	}
}