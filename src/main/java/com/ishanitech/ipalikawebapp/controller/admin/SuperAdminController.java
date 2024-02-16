package com.ishanitech.ipalikawebapp.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.RoleDTO;
import com.ishanitech.ipalikawebapp.dto.UserDTO;
import com.ishanitech.ipalikawebapp.service.FormService;
import com.ishanitech.ipalikawebapp.service.UserService;
import com.ishanitech.ipalikawebapp.service.WardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/super-admin")
@Controller
public class SuperAdminController {
	private final UserService userService;
	private final FormService formService;
	private final WardService wardService;

	public SuperAdminController(FormService formService, UserService userService, WardService wardService) {
		this.formService = formService;
		this.userService = userService;
		this.wardService = wardService;
	}
	
	@GetMapping
	public String getAdminPage() {
		return "private/super-admin/index";
	}
	
	@GetMapping("/edit/{userId}")
	public String getEditUserInfoPage(Model model, @AuthenticationPrincipal UserDTO user, @PathVariable("userId") int userId) {
		model.addAttribute("userInfo", userService.getUserInfoByUserId(userId, user.getToken()).getData());
		
		List<RoleDTO> roles = new ArrayList<>();
		if(user.getRoles().get(0).equalsIgnoreCase("SUPER_ADMIN")) {
			roles = userService.getAllRoles(user.getToken()).getData();
		} else {
			roles = userService.getAllRoles(user.getToken()).getData()
					.stream()
					.filter(role -> !role.getRole().equalsIgnoreCase("SUPER_ADMIN")
							&& !role.getRole().equalsIgnoreCase("CENTRAL_ADMIN"))
					.collect(Collectors.toList());
		}
		
		model.addAttribute("roles", roles);
		model.addAttribute("wards", wardService.getAllWards());
		
		return "private/super-admin/edit-user-info";
	}
	
	@PatchMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response<String> changeUserInfoByAdmin(@PathVariable("userId") int userId,
			@RequestBody Map<String, Object> updates,
			@AuthenticationPrincipal UserDTO user) {
		userService.updateUserInfoByUserIdByAdmin(updates, userId, user.getToken());
		return new Response<String>("Successfully updated information");
	}
	
	@GetMapping("password/{userId}")
	public String getPasswordChangePage(Model model, @AuthenticationPrincipal UserDTO adminuser, @PathVariable("userId") int userId) {
		model.addAttribute("userId", userId);
		return "private/super-admin/edit-user-password";
	}
	
	@PutMapping("/{userId}/password")
	public@ResponseBody  Response<String> changePassword(@RequestBody String newPassword, 
			@PathVariable("userId") int userId, 
			@AuthenticationPrincipal UserDTO loggedInUser) {
		userService.changePasswordByAdmin(newPassword, userId, loggedInUser.getToken());
		return new Response<String>("Successfully changed the password");
	}
}
