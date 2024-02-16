/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Feb 18, 2020
 */
package com.ishanitech.ipalikawebapp.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.ToleDTO;
import com.ishanitech.ipalikawebapp.dto.WardDTO;

public interface WardService {
	List<Integer> getAllWards();

	void addWard(WardDTO wardInfo, String token);

	Response<WardDTO> getWardByWardNumber(int wardNo, String token);

	Response<List<WardDTO>> getAllWardInfo(String token);

	void deleteWard(int wardNo, String token);

	void editWard(WardDTO wardInfo, int wardNo, String token);

	void addWardBuildingImage(MultipartFile multipartFile, String imageName, String token);
	
	public Response<List<ToleDTO>> getListOfToles();
}
