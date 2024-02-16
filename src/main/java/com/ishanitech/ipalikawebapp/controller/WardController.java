package com.ishanitech.ipalikawebapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ishanitech.ipalikawebapp.configs.properties.UploadDirectoryProperties;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.UserDTO;
import com.ishanitech.ipalikawebapp.dto.WardDTO;
import com.ishanitech.ipalikawebapp.service.WardService;


@RequestMapping("/ward")
@Controller
public class WardController {
	private final WardService wardService;
	private final UploadDirectoryProperties uploadDirectoryProperties;

	public WardController(WardService wardService, UploadDirectoryProperties uploadDirectoryProperties) {
		this.wardService = wardService;
		this.uploadDirectoryProperties = uploadDirectoryProperties;
	}

	
	@Secured({"ROLE_CENTRAL_ADMIN"})
	@GetMapping
	public String getWardListView(@AuthenticationPrincipal UserDTO user, Model model) {
		Response<List<WardDTO>> wardResponse = wardService.getAllWardInfo(user.getToken());
		model.addAttribute("wardList", wardResponse.getData());
		return "private/central-admin/ward-list";
	}
	
	@Secured({"ROLE_CENTRAL_ADMIN"})
	@GetMapping("/add")
	public String getWardEntryView() {
		return "private/central-admin/add-ward";
	}

	@Secured({"ROLE_CENTRAL_ADMIN"})
	@PostMapping
	public @ResponseBody Response<String> addWard(@RequestBody WardDTO wardInfo, @AuthenticationPrincipal UserDTO user) {
		wardService.addWard(wardInfo, user.getToken());
		return new Response<String>("Ward successfully added!");
	}
	
	
	@Secured({"ROLE_CENTRAL_ADMIN"})
	@PostMapping("/image")
	public @ResponseBody String addWardBuildingImage(MultipartHttpServletRequest request, @AuthenticationPrincipal UserDTO user) {
		String inputTagName = request.getParameter("imgIndex");
		String fileName = request.getParameter("fileName");
		Path rootLocation = Paths.get(uploadDirectoryProperties.getTempFileUploadingDirectory());
		try {
			MultipartFile favPlaceImage = request.getFile(inputTagName);
			
			String imageName = fileName;
			
			//For copying the file to upload directory
			Files.copy(favPlaceImage.getInputStream(), rootLocation.resolve(imageName));
			
			//For retrieving saved multipart file
			File file = new File(uploadDirectoryProperties.getTempFileUploadingDirectory() + imageName);
			FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
			
			InputStream input = null;
			OutputStream os = null;
			
			try {
				input = new FileInputStream(file);
				os = fileItem.getOutputStream();
				IOUtils.copy(input, os);
			} catch (IOException ex) {
				
			} finally {
				if(input != null) {
					input.close();
				}
				if(os != null) {
					os.close();
				}
			}
			
			MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
			
			wardService.addWardBuildingImage(multipartFile, imageName, user.getToken());
			
		} catch(Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		
		try {
			String imageName = fileName;
			Path path = Paths.get(uploadDirectoryProperties.getTempFileUploadingDirectory() + imageName);
			Files.delete(path);
		} catch(NoSuchFileException e) {
			System.out.println("No such file/directory exists");
		} catch(DirectoryNotEmptyException e) {
			System.out.println("Directory is not empty");
		} catch(IOException e) {
			System.out.println("Invalid Permissions.");
			e.printStackTrace();
		}
		return "1";
	}
	
	
	
	@GetMapping("/{wardNumber}")
	public String getWardInfoByWardNumber(@PathVariable("wardNumber") int wardNo, @AuthenticationPrincipal UserDTO user, Model model) {
		if(user.getRoles().contains("WARD_ADMIN")) {
			Response<WardDTO> wardResponse = wardService.getWardByWardNumber(user.getWardNo(), user.getToken());
			model.addAttribute("wardInfo", wardResponse.getData());
		} else {
			Response<WardDTO> wardResponse = wardService.getWardByWardNumber(wardNo, user.getToken());
			model.addAttribute("wardInfo", wardResponse.getData());
		}
		return "private/common/ward-details";
	}
	
	@DeleteMapping("/{wardNumber}")
	public @ResponseBody Response<String> deleteWard(@PathVariable("wardNumber") int wardNo, @AuthenticationPrincipal UserDTO user) {
		wardService.deleteWard(wardNo, user.getToken());  
		return new Response<String>("Ward removed successfully!");
	}
	
	@Secured({"ROLE_CENTRAL_ADMIN", "ROLE_WARD_ADMIN", "ROLE_SUPER_ADMIN"})
	@GetMapping("/edit/{wardNumber}")
	public String getWardEditView(@PathVariable("wardNumber") int wardNo, @AuthenticationPrincipal UserDTO user, Model model) {
		if(user.getRoles().contains("WARD_ADMIN")) {
			Response<WardDTO> wardResponse = wardService.getWardByWardNumber(user.getWardNo(), user.getToken());
			model.addAttribute("wardInfo", wardResponse.getData());
		} else {
			Response<WardDTO> wardResponse = wardService.getWardByWardNumber(wardNo, user.getToken());
			model.addAttribute("wardInfo", wardResponse.getData());
		}
		return "private/common/edit-ward";
	}
	
	@Secured({"ROLE_CENTRAL_ADMIN", "ROLE_WARD_ADMIN", "ROLE_SUPER_ADMIN"})
	@PutMapping("/{wardNumber}")
	public @ResponseBody Response<String> editWard(@RequestBody WardDTO wardInfo, @PathVariable("wardNumber") int wardNo, @AuthenticationPrincipal UserDTO user) {
		if(user.getRoles().contains("WARD_ADMIN")) {
			wardService.editWard(wardInfo, user.getWardNo(), user.getToken());
		} else {
		wardService.editWard(wardInfo, wardNo, user.getToken());
		}
		return new Response<String>("Ward details edited successfully");
	}
	
}
