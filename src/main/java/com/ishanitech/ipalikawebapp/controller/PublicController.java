package com.ishanitech.ipalikawebapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ishanitech.ipalikawebapp.dto.ResidentDTO;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.service.PublicService;
import com.ishanitech.ipalikawebapp.service.WardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/public/resident")
@Controller
public class PublicController {
	
	private final WardService wardService;
	private final PublicService publicService;
	
	public PublicController(WardService wardService, PublicService publicService) {
		this.wardService = wardService;
		this.publicService = publicService;
	}
	
	@GetMapping
	public String getResidentDataListView(Model model, @RequestParam(name = "backFrom", required= false) String backFrom) {
		Response<List<ResidentDTO>> residentResponse;
		
		residentResponse = (Response<List<ResidentDTO>>) publicService
				.getResidentDataList();
		model.addAttribute("residentList", residentResponse.getData());
		
		model.addAttribute("wards", wardService.getAllWards());
		model.addAttribute("toles", wardService.getListOfToles().getData());
		return "public/resident-data";
	}
	
	
	
	@PostMapping("/search")
	public @ResponseBody List<ResidentDTO> getResidentsBySearchKey(HttpServletRequest request, @RequestParam("searchKey") String searchKey, @RequestParam("wardNo") String wardNo) {
		log.info("WardNo---->" + wardNo);
		return publicService.searchResidentByKey(request, searchKey, wardNo);
	}
	
	@PostMapping("/ward")
	public @ResponseBody List<ResidentDTO> getResidentsByWard(@RequestParam("wardNo") String wardNo,HttpServletRequest request) {
		log.info("WardNo---->" + wardNo);
		log.info("PagedLimited---->" + request.getParameter("pageSize"));
		return publicService.searchResidentByWard(request, wardNo);
	}
	
	
	@PostMapping("/tole")
	public @ResponseBody List<ResidentDTO> getResidentsByTole(@RequestParam("wardNo") String wardNo,HttpServletRequest request) {
		log.info("WardNo---->" + wardNo);
		log.info("PagedLimited---->" + request.getParameter("pageSize"));
		return publicService.searchResidentByTole(request, wardNo);
	}
	
	@PostMapping("/pageLimit")
	public @ResponseBody List<ResidentDTO> getResidentsByPageLimit(@RequestParam("wardNo") String wardNo,HttpServletRequest request) {
		log.info("WardNo---->" + wardNo);
		log.info("PagedLimited---->" + request.getParameter("pageSize"));
		return publicService.getResidentByPageLimit(request, wardNo);
	}
	
	@PostMapping("/nextLot")
	public @ResponseBody List<ResidentDTO> getNextLotResidents(HttpServletRequest request) {
		return publicService.getNextLotResidents(request);
	}
	
	@PostMapping("/sortBy")
	public @ResponseBody List<ResidentDTO> getSortedResidents(HttpServletRequest request) {
		return publicService.getSortedResidents(request);
	}
	
	
	@GetMapping("getTotalHouseCount/{wardNo}")
	public @ResponseBody Response<String> getTotalHouseCountByWard(@PathVariable("wardNo") String wardNo, Model model, @RequestParam(name="toleName", required=false) String toleName) {
		if(toleName != null)
		System.out.println("ToleName Req Parm--->"+ toleName);
		return new Response<String>(publicService.getTotalHouseCountByWard(wardNo, toleName));
	}




}
