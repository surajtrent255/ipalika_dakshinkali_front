package com.ishanitech.ipalikawebapp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ishanitech.ipalikawebapp.dto.FamilyMemberDTO;
import com.ishanitech.ipalikawebapp.dto.ResidentDTO;
import com.ishanitech.ipalikawebapp.dto.Response;

public interface ResidentService {
	Response<?> getResidentDataList(String token, List<String> roles, int wardNumber, HttpServletRequest httpServletRequest);
	List<ResidentDTO> searchResidentByKey(HttpServletRequest request,String searchKey,String wardNo, String token);
	List<ResidentDTO> searchResidentByWard(HttpServletRequest request, String wardNo, String token);
	Response<?> getResidentFullDetail(String filledId, String token);
	Response<?> getResidentFullDetailRaw(String filledId, String token);
	void addFamilyMember(FamilyMemberDTO familyMemberInfo, String token);
	Response<?> getMemberFormDetails(String token);
	Response<?> getMemberByMemberId(String token, String memberId);
	void editFamilyMemberInfo(FamilyMemberDTO familyMemberInfo, String memberId, String token);
	void deleteFamilyMember(String memberId, String token);
	void deleteHouseholdByFamilyId(String familyId, String token);
	List<ResidentDTO> getNextLotResidents(HttpServletRequest request,List<String> roles, int wardNumber, String token);
	List<ResidentDTO> getResidentByPageLimit(HttpServletRequest request, String wardNo, String token);
	List<ResidentDTO> getSortedResidents(HttpServletRequest request, String token);

	String getTotalHouseCountByWard(String wardNo, String toleName, String token);

	List<ResidentDTO> searchResidentByTole(HttpServletRequest request, String wardNo, String token);
}
