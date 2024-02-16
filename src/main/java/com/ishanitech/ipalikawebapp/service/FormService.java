package com.ishanitech.ipalikawebapp.service;

import java.util.List;

import com.ishanitech.ipalikawebapp.dto.FormDetail;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.ToleDTO;

public interface FormService {
	public List<FormDetail> getFullFormDetailById(int id, String token);

	public Response<List<String>> getListofDistricts(String token);

	public Response<List<String>> getListOfWards(String token);

	public Response<List<ToleDTO>> getListOfToles(String token);
}
