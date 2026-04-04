package com.sp.app.service;

import java.util.Map;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sp.app.domain.dto.MemberDto;
import com.sp.app.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
	private final MemberMapper mapper;
	private final PasswordEncoder bcryptEncoder;
	
	@Override
	public MemberDto loginSnsMember(Map<String, Object> map) {
		return mapper.loginSnsMember(map);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void insertMember(MemberDto dto) throws Exception {
		try {
			Long seq = mapper.memberSeq();
			dto.setMember_id(seq);
			dto.setPassword(bcryptEncoder.encode(dto.getPassword()));

			mapper.insertMember1(dto);
			mapper.insertMember2(dto);

			dto.setAuthority("USER");
			mapper.insertAuthority(dto);
		} catch (Exception e) {
			log.error("insertMember 에러: ", e);
			throw e;
		}
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void insertSnsMember(MemberDto dto) throws Exception {
		try {
			Long seq = mapper.memberSeq();
			dto.setMember_id(seq);

			if (dto.getLogin_id() == null || dto.getLogin_id().isBlank()) {
				dto.setLogin_id(dto.getSns_provider() + "_" + dto.getSns_id());
			}
			if (dto.getPassword() == null || dto.getPassword().isBlank()) {
				dto.setPassword(bcryptEncoder.encode("sns_dummy_password"));
			}
			if (dto.getBirth() == null || dto.getBirth().isBlank()) {
				dto.setBirth("2000-01-01");
			}

			mapper.insertMember1(dto);
			mapper.insertMember2(dto);

			dto.setAuthority("USER");
			mapper.insertAuthority(dto);
		} catch (Exception e) {
			log.error("insertSnsMember 에러: ", e);
			throw e;
		}
	}

	@Override
	public void updatePassword(MemberDto dto) throws Exception {
		try {
			dto.setPassword(bcryptEncoder.encode(dto.getPassword()));
			mapper.updateMemberPassword(dto);
		} catch (Exception e) {
			log.error("updatePassword 에러: ", e);
			throw e;
		}
	}

	@Override
	public void updateMemberEnabled(Map<String, Object> map) throws Exception {
		mapper.updateMemberEnabled(map);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void updateMember(MemberDto dto) throws Exception {
		try {
			MemberDto current = mapper.findById(dto.getMember_id());
			dto.setLogin_id(current.getLogin_id());

			if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
				dto.setPassword(bcryptEncoder.encode(dto.getPassword()));
				mapper.updateMemberPassword(dto);
			}
			mapper.updateMember2(dto);
		} catch (Exception e) {
			log.error("updateMember 에러: ", e);
			throw e;
		}
	}

	@Override
	public void updateLastLogin(Long member_id) throws Exception {
		mapper.updateLastLogin(member_id);
	}

	@Override
	public void updateLastLogin(String login_id) throws Exception {
		mapper.updateLastLoginId(login_id);
	}

	@Override
	public MemberDto findById(Long member_id) {
		return mapper.findById(member_id);
	}

	@Override
	public MemberDto findById(String login_id) {
		return mapper.findByLoginId(login_id);
	}

	@Override
	public Long getMemberId(String login_id) {
		return mapper.getMemberId(login_id);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void deleteMember(Map<String, Object> map) throws Exception {
		mapper.deleteAuthority(map);
		map.put("enabled", 0);
		mapper.updateMemberEnabled(map);
		mapper.deleteMember2(map);
	}

	@Override
	public String findByAuthority(String login_id) {
		return mapper.findByAuthority(login_id);
	}

	@Override
	public boolean isPasswordCheck(String login_id, String password) {
		try {
			MemberDto dto = mapper.findByLoginId(login_id);
			return bcryptEncoder.matches(password, dto.getPassword());
		} catch (Exception e) {
			return false;
		}
	}
}