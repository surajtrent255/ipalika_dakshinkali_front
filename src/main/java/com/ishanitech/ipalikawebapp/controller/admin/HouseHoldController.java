package com.ishanitech.ipalikawebapp.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ishanitech.ipalikawebapp.dto.FormDetail;
import com.ishanitech.ipalikawebapp.dto.ResidentDetailDTO;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.UserDTO;
import com.ishanitech.ipalikawebapp.service.FormService;
import com.ishanitech.ipalikawebapp.service.ResidentService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/household")
@Controller
@RequiredArgsConstructor
public class HouseHoldController {

	private final FormService formService;
	private final ResidentService residentService;

	@GetMapping("/{filledFormId}")
	public String getHouseHoldPage(@PathVariable("filledFormId") String filledId, Model model, @AuthenticationPrincipal UserDTO user) {
			Response<ResidentDetailDTO> residentResponse = (Response<ResidentDetailDTO>) residentService.getResidentFullDetail(filledId, user.getToken());
		model.addAttribute("residentFullDetail", residentResponse.getData());
		
		// Added for tabbed layout
		List<FormDetail> formDetails = formService.getFullFormDetailById(1, user.getToken());
		List<String> questionTypeTabsWithSpacing = new ArrayList<String>();
		model.addAttribute("questionAndOptions", formDetails);
		List<String> questionTypeTabs = new ArrayList<String>();
		for (int i = 13; i < formDetails.size(); i++) {
			if (!questionTypeTabs.contains(formDetails.get(i).getGrouping().replaceAll("\\s+", ""))) {
				questionTypeTabs.add(formDetails.get(i).getGrouping().replaceAll("\\s+", ""));
				questionTypeTabsWithSpacing.add(formDetails.get(i).getGrouping());
			}
		}

		model.addAttribute("qustionTypeTabs", questionTypeTabs);
		model.addAttribute("qustionTypeTabsWithSpacing", questionTypeTabsWithSpacing);
		return  "private/common/resident-details";
	}
}
