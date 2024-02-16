package com.ishanitech.ipalikawebapp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ishanitech.ipalikawebapp.dto.ResidentDTO;
import com.ishanitech.ipalikawebapp.dto.Response;

public interface PublicService {
	Response<?> getResidentDataList();
	List<ResidentDTO> searchResidentByKey(HttpServletRequest request,String searchKey,String wardNo);
	List<ResidentDTO> searchResidentByWard(HttpServletRequest request, String wardNo );
	List<ResidentDTO> getNextLotResidents(HttpServletRequest request);
	List<ResidentDTO> getResidentByPageLimit(HttpServletRequest request, String wardNo );
	List<ResidentDTO> getSortedResidents(HttpServletRequest request );

	String getTotalHouseCountByWard(String wardNo, String toleName );

	List<ResidentDTO> searchResidentByTole(HttpServletRequest request, String wardNo );
}
