package com.ishanitech.ipalikawebapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.RoleDTO;
import com.ishanitech.ipalikawebapp.dto.UserDTO;
import com.ishanitech.ipalikawebapp.dto.UserRegistrationDTO;
import com.ishanitech.ipalikawebapp.service.UserService;
import com.ishanitech.ipalikawebapp.service.WardService;
import com.ishanitech.ipalikawebapp.utilities.UserDetailsUtil;


@RequestMapping("/user")
@Controller
public class UserController {
	private final UserService userService;
	private final WardService wardService;
	
	public UserController(UserService userService, WardService wardService) {
		this.userService = userService;
		this.wardService = wardService;
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_CENTRAL_ADMIN"})
	@PostMapping
	public @ResponseBody Response<String> addUser(@RequestBody UserRegistrationDTO user, @AuthenticationPrincipal UserDTO loggedInUser) {
		userService.addUser(user, UserDetailsUtil.getToken(loggedInUser));
		return new Response<String>("User is created!");
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_CENTRAL_ADMIN"})
	@GetMapping("/add")
	public String addUser(Model model, @AuthenticationPrincipal UserDTO user) {
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
		return "private/common/add-user";
	}
	
	@GetMapping("/profile")
	public String userProfilePage() {
		return "private/common/user-profile";
	}
	
	@GetMapping("/setting")
	public String userSettingPage() {
		return "private/common/user-settings";
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_CENTRAL_ADMIN"})
	@DeleteMapping("/{userId}")
	public @ResponseBody Response<String> deleteUserById(@PathVariable("userId") int userId, @AuthenticationPrincipal UserDTO loggedInUser) {
		userService.deleteUser(userId, UserDetailsUtil.getToken(loggedInUser));
		return new Response<String>("Successfully deleted the user.");
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_CENTRAL_ADMIN"})
	@PutMapping("/{userId}/disable")
	public @ResponseBody Response<String> disableUserById(@PathVariable("userId") int userId, @AuthenticationPrincipal UserDTO loggedInUser) {
		userService.disableUser(userId, UserDetailsUtil.getToken(loggedInUser));
		return new Response<String>("Successfully disabled user.");
	}
	
	@PutMapping("/{userId}/password")
	public@ResponseBody  Response<String> changePassword(@RequestBody String newPassword, 
			@PathVariable("userId") int userId, 
			@AuthenticationPrincipal UserDTO loggedInUser) {
		userService.changePassword(newPassword, userId, UserDetailsUtil.getToken(loggedInUser));
		return new Response<String>("Successfully changed the password");
	}
	
	@PatchMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response<String> changeUserInfo(@PathVariable("userId") int userId,
			@RequestBody Map<String, Object> updates,
			@AuthenticationPrincipal UserDTO loggedInUser) {
		userService.updateUserInfoByUserId(updates, userId, UserDetailsUtil.getToken(loggedInUser));
		
		UserDTO user = loggedInUser; // Assign login user to new userDTO
		user.setFullName((String)updates.get("fullName")); // update full name of new userDTO
		// Create a new authentication object
		Authentication auth = new UsernamePasswordAuthenticationToken(loggedInUser, null ,SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		// Set out new auth object to security context holder. This will update our previous authentication principal and we don't have to logout from application.
		SecurityContextHolder.getContext().setAuthentication(auth);
		return new Response<String>("Successfully updated information");
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_CENTRAL_ADMIN"})
	@GetMapping("/userList")
	public String getUserListPage(Model model, @AuthenticationPrincipal UserDTO loggedInUser) {
		model.addAttribute("userInfo", userService.getAllUserInfo(loggedInUser.getToken()).getData());
		return "private/common/user-list";
	}
	
	
	@PostMapping(value = "/duplicate")
	public @ResponseBody Map<String, Boolean> checkValuesForPotentialDuplicates(@AuthenticationPrincipal UserDTO user, @RequestBody Map<String, String> paramToCheck) {
		return userService.checkPotentialDuplicateColumns(paramToCheck, user.getToken());
	}
}
