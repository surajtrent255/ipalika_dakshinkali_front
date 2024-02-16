package com.ishanitech.ipalikawebapp.service;


import java.util.List;
import java.util.Map;

import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.RoleDTO;
import com.ishanitech.ipalikawebapp.dto.UserDTO;
import com.ishanitech.ipalikawebapp.dto.UserRegistrationDTO;

public interface UserService {
	public Response<List<RoleDTO>> getAllRoles(String token);
	Response<?> addUser(UserRegistrationDTO user, String token);
	public void deleteUser(int userId, String token);
	public void changePassword(String newPassword, int userId, String token);
	public void updateUserInfoByUserId(Map<String, Object> updates, int userId, String token);
	public void disableUser(int userId, String token);
	public Response<List<UserDTO>> getAllUserInfo(String token);
	public Map<String, Boolean> checkPotentialDuplicateColumns(Map<String, String> params, String token);
	public Response<UserDTO> getUserInfoByUserId(int userId, String token);
	void updateUserInfoByUserIdByAdmin(Map<String, Object> updates, int userId, String token);
	void changePasswordByAdmin(String newPassword, int userId, String token);
}
