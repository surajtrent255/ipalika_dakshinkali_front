package com.ishanitech.ipalikawebapp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ishanitech.ipalikawebapp.dto.FamilyMemberDTO;
import com.ishanitech.ipalikawebapp.dto.Response;

public interface MemberService {

	Response<?> getMemberDataList(String token, List<String> roles, int wardNumber, HttpServletRequest httpServletRequest);

	List<FamilyMemberDTO> searchMemberByKey(HttpServletRequest request, String searchKey, String wardNo, String token);

	List<FamilyMemberDTO> getMemberByWard(HttpServletRequest request, String wardNo, String token);

	List<FamilyMemberDTO> getMemberByPageLimit(HttpServletRequest request, String wardNo, String token);

	List<FamilyMemberDTO> getNextLotMembers(HttpServletRequest request, List<String> roles, int wardNo, String token);

	List<FamilyMemberDTO> getSortedMembers(HttpServletRequest request, String token);
}
