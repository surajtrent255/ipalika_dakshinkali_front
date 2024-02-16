package com.ishanitech.ipalikawebapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ishanitech.ipalikawebapp.dto.FamilyMemberDTO;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.UserDTO;
import com.ishanitech.ipalikawebapp.service.MemberService;
import com.ishanitech.ipalikawebapp.service.ResidentService;
import com.ishanitech.ipalikawebapp.service.WardService;


@Secured({"ROLE_CENTRAL_ADMIN", "ROLE_WARD_ADMIN", "ROLE_SURVEYOR"})
@RequestMapping("/member")
@Controller
public class MemberController {

	private final MemberService memberService;
	private final WardService wardService;
	private final ResidentService residentService;
	
	public MemberController(MemberService memberService, WardService wardService, ResidentService residentService) {
		this.memberService = memberService;
		this.wardService = wardService;
		this.residentService = residentService;
	}
	
	@GetMapping
	public String getMemberDataListView(Model model, @AuthenticationPrincipal UserDTO user,
										@RequestParam(name = "backFrom", required= false) String backFrom,
										HttpServletRequest httpServletRequest) {
		Response<List<FamilyMemberDTO>> 	 memberResponse = (Response<List<FamilyMemberDTO>>) memberService.getMemberDataList(user.getToken(), user.getRoles(), user.getWardNo(), httpServletRequest);
		model.addAttribute("memberList", memberResponse.getData());
		model.addAttribute("wards", wardService.getAllWards());
		model.addAttribute("loggedInUserWard", user.getWardNo());
		return "private/common/family-member-data";
	}
	
	@PostMapping("/search")
	public @ResponseBody List<FamilyMemberDTO> getMemberBySearchKey(HttpServletRequest request, @RequestParam("searchKey") String searchKey, @RequestParam("wardNo") String wardNo, @AuthenticationPrincipal UserDTO user) {
		return memberService.searchMemberByKey(request, searchKey, wardNo, user.getToken());
	}
	
	@PostMapping("/ward")
	public @ResponseBody List<FamilyMemberDTO> getMemberByward(HttpServletRequest request, @RequestParam("wardNo") String wardNo, @AuthenticationPrincipal UserDTO user) {
		return memberService.getMemberByWard(request, wardNo, user.getToken());
	}
	
	@PostMapping("/pageLimit")
	public @ResponseBody List<FamilyMemberDTO> getMemberByPageLimit(HttpServletRequest request, @RequestParam("wardNo") String wardNo, @AuthenticationPrincipal UserDTO user) {
		return memberService.getMemberByPageLimit(request, wardNo, user.getToken());
	}
	
	@PostMapping("/nextLot")
	public @ResponseBody List<FamilyMemberDTO> getNextLotMembers(HttpServletRequest request, @AuthenticationPrincipal UserDTO user) {
		return memberService.getNextLotMembers(request, user.getRoles(), user.getWardNo(), user.getToken());
	}
	
	@PostMapping("/sortBy")
	public @ResponseBody List<FamilyMemberDTO> getSortedMembers(HttpServletRequest request, @AuthenticationPrincipal UserDTO user) {
		return memberService.getSortedMembers(request, user.getToken());
	}

	@GetMapping("/details/{memberId}")
	public String getMemberDetailsView(Model model,@PathVariable("memberId") String memberId, @AuthenticationPrincipal UserDTO user) {
		Response<FamilyMemberDTO> memberResponse = (Response<FamilyMemberDTO>) residentService.getMemberByMemberId(user.getToken(), memberId);
		model.addAttribute("memberInfo", memberResponse.getData());
		return "private/common/member-details";
	}
}
