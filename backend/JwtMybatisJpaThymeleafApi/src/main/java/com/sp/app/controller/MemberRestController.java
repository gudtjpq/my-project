package com.sp.app.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sp.app.domain.dto.MemberDto;
import com.sp.app.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/*")
@Slf4j
public class MemberRestController {
	private final MemberService memberService;

	@GetMapping("info/{member_id}")
	public ResponseEntity<?> getInfo(@PathVariable("member_id") Long member_id) {
		try {
			MemberDto dto = memberService.findById(member_id);
			return ResponseEntity.ok(Map.of("data", dto));
		} catch (Exception e) {
			log.error("getInfo 에러: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("조회 실패");
		}
	}

	@PutMapping("update")
	public ResponseEntity<?> update(@RequestBody MemberDto dto) {
		try {
			memberService.updateMember(dto);
			return ResponseEntity.ok(Map.of("state", "true"));
		} catch (Exception e) {
			log.error("update 에러: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("state", "false"));
		}
	}
}