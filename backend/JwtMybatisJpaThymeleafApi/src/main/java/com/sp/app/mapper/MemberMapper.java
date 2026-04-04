package com.sp.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.dto.MemberDto;
import com.sp.app.domain.dto.RefreshTokenDto;

@Mapper
public interface MemberMapper {
	public MemberDto loginSnsMember(Map<String, Object> map);
	public Long memberSeq();
	
	public void insertMember1(MemberDto dto);
	public void insertMember2(MemberDto dto);
	public void insertAuthority(MemberDto dto);
	
	public void updateMemberPassword(MemberDto dto);
	public void updateMemberEnabled(Map<String, Object> map);
	public void updateMember2(MemberDto dto);
	
	public void updateLastLogin(Long member_id);
	public void updateLastLoginId(String login_id);
	
	public MemberDto findByLoginId(String login_id);
	public MemberDto findById(Long member_id);
	public Long getMemberId(String login_id);
	public String findByAuthority(String login_id);
	
	public void deleteAuthority(Map<String, Object> map);
	public void deleteMember1(Map<String, Object> map);
	public void deleteMember2(Map<String, Object> map);
	
	public void insertRefreshToken(RefreshTokenDto dto);
	public void updateRefreshToken(RefreshTokenDto dto);
	public RefreshTokenDto findByToken(String login_id);
	
	public int checkFailureCount(String login_id);
	public void updateFailureCountReset(String login_id);
	public void updateFailureCount(String login_id);
	public List<MemberDto> listFindMember(Map<String, Object> map);
}