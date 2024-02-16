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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ishanitech.ipalikawebapp.configs.properties.UploadDirectoryProperties;
import com.ishanitech.ipalikawebapp.dto.AnswerDTO;
import com.ishanitech.ipalikawebapp.dto.FormDetail;
import com.ishanitech.ipalikawebapp.dto.Response;
import com.ishanitech.ipalikawebapp.dto.UserDTO;
import com.ishanitech.ipalikawebapp.service.FormService;
import com.ishanitech.ipalikawebapp.service.ResidentService;
import com.ishanitech.ipalikawebapp.service.SurveyAnswerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/survey-answer")
@Controller
public class SurveyAnswerController {
	private final FormService formService;
	private final SurveyAnswerService surveyAnswerService;
	private final ResidentService residentService;
	private final UploadDirectoryProperties uploadDirectoryProperties;

	public SurveyAnswerController(FormService formService, SurveyAnswerService surveyAnswerService, ResidentService residentService,
			UploadDirectoryProperties uploadDirectoryProperties) {
		super();
		this.formService = formService;
		this.surveyAnswerService = surveyAnswerService;
		this.residentService = residentService;
		this.uploadDirectoryProperties = uploadDirectoryProperties;
	}

	@GetMapping("/household")
	public String getHouseholdEntryForm(Model model, @AuthenticationPrincipal UserDTO user) {
		model.addAttribute("answerObj", new AnswerDTO());
		
		//Added
		List<FormDetail> formDetails = formService.getFullFormDetailById(1, user.getToken());
		List<FormDetail> newFormDetails = new ArrayList<FormDetail>();
		for(FormDetail formDetail: formDetails) {
			formDetail.setGrouping(formDetail.getGrouping().replaceAll("\\s+", ""));
			newFormDetails.add(formDetail);
		}
		
		model.addAttribute("questionAndOptions", newFormDetails);
		model.addAttribute("districts", formService.getListofDistricts(user.getToken()).getData());
		model.addAttribute("wards", formService.getListOfWards(user.getToken()).getData());
		
		// Added for tabbed layout
				List<FormDetail> questionAndOptions = formService.getFullFormDetailById(1, user.getToken());
				List<String> questionTypeTabs = new ArrayList<String>();
				List<String> questionTypeTabsWithSpacing = new ArrayList<String>();
				for (int i = 13; i < questionAndOptions.size(); i++) {
					String tabName = questionAndOptions.get(i).getGrouping().replaceAll("\\s+", "");
					if (!questionTypeTabs.contains(tabName)) {
						questionTypeTabs.add(tabName);
						questionTypeTabsWithSpacing.add(questionAndOptions.get(i).getGrouping());
					}
				}

				model.addAttribute("qustionTypeTabs", questionTypeTabs);
				model.addAttribute("qustionTypeTabsWithSpacing", questionTypeTabsWithSpacing);

				// Added for tabbed layout ends
		
		return "private/common/add-household";
	}
	
	@GetMapping("/household/edit/{filledFormId}")
	public String getHouseholdEditForm(Model model, @AuthenticationPrincipal UserDTO user, @PathVariable("filledFormId") String filledId) {
		
		//Added for tabbed layout
				List<FormDetail> formDetails = formService.getFullFormDetailById(1, user.getToken());
				List<FormDetail> newFormDetails = new ArrayList<FormDetail>();
				for(FormDetail formDetail: formDetails) {
					formDetail.setGrouping(formDetail.getGrouping().replaceAll("\\s+", ""));
					newFormDetails.add(formDetail);
				}
				
				model.addAttribute("questionAndOptions", newFormDetails);
		//Added for tabbbed layout ends
				
		model.addAttribute("answerObj", new AnswerDTO());
		//model.addAttribute("questionAndOptions", formService.getFullFormDetailById(1, user.getToken()));
		model.addAttribute("districts", formService.getListofDistricts(user.getToken()).getData());
		model.addAttribute("wards", formService.getListOfWards(user.getToken()).getData());
		
		//for editing purpose
		Response<AnswerDTO> residentResponse = (Response<AnswerDTO>) residentService.getResidentFullDetailRaw(filledId, user.getToken());
		model.addAttribute("residentFullDetail", residentResponse.getData());
		
		//ends
		
		// Added for tabbed layout
		List<FormDetail> questionAndOptions = formService.getFullFormDetailById(1, user.getToken());
		List<String> questionTypeTabs = new ArrayList<String>();
		List<String> questionTypeTabsWithSpacing = new ArrayList<String>();
		for (int i = 13; i < questionAndOptions.size(); i++) {
			String tabName = questionAndOptions.get(i).getGrouping().replaceAll("\\s+", "");
			if (!questionTypeTabs.contains(tabName)) {
				questionTypeTabs.add(tabName);
				questionTypeTabsWithSpacing.add(questionAndOptions.get(i).getGrouping());
			}
		}

		model.addAttribute("qustionTypeTabs", questionTypeTabs);
		model.addAttribute("qustionTypeTabsWithSpacing", questionTypeTabsWithSpacing);

		// Added for tabbed layout ends
		
		return "private/common/edit-household";
	}
	
	@PostMapping("/household")
	public @ResponseBody
    int addHouseHold(@RequestBody AnswerDTO answerDto, @AuthenticationPrincipal UserDTO user, HttpServletRequest httpServletRequest) {
        
       log.info("AnserDTO : " + answerDto.toString());
        answerDto.setEntryDate(LocalDateTime.now().toString());
        answerDto.setAddedBy(user.getUserId());
        
        try {
        	surveyAnswerService.addHouseholdSurveyAnswer(answerDto, user.getToken());
        	return 1;
        }catch(Exception e) {
        	e.printStackTrace();
        	log.info(e.getMessage());
        	return 0;
        }
        //return 1;
    }
	@RequestMapping(value = "/image", method = RequestMethod.POST)
	public @ResponseBody String addImage(MultipartHttpServletRequest request, @AuthenticationPrincipal UserDTO user) {
		
		String inputTagName = request.getParameter("imgIndex");
		System.out.println("InputTagName----> " + inputTagName);
		String extension = request.getParameter("extension");
		String filledId = request.getParameter("filledId");
		String questionId = request.getParameter("questionId");
		Path rootLocation = Paths.get(uploadDirectoryProperties.getTempFileUploadingDirectory());
		try {
			MultipartFile ourImage = request.getFile(inputTagName);
			//ourImage.getOriginalFilename().replace(ourImage.getOriginalFilename(), "JPEG_" + filledId + "_" + questionId + "." + extension);
			
			String imageName = "JPEG_" + filledId + "_" + "1_"+ questionId + "." + extension;
			//ourImage.getOriginalFilename().concat(imageName);
			System.out.println("GeneratedImageName----->" + imageName);
			System.out.println(ourImage.getSize());
			System.out.println(ourImage.getOriginalFilename());
			System.out.println("Name----->" + ourImage.getName());
			
			//for copying file to upload directory
			Files.copy(ourImage.getInputStream(), rootLocation.resolve(imageName));
			
			//for retrieving saved multipart file
			File file = new File(uploadDirectoryProperties.getTempFileUploadingDirectory() + imageName);
			FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

			InputStream input = null;
			OutputStream os = null;
			
			try {
			     input = new FileInputStream(file);
			     os = fileItem.getOutputStream();
			    IOUtils.copy(input, os);
			    // Or faster..
			    // IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
			} catch (IOException ex) {
			    // do something.
			}finally {
				if(input != null) {
					input.close();
				}
				if(os!= null) {
					os.close();
				}
			}

			MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
			//ends
			
			surveyAnswerService.addPhoto(multipartFile, imageName, user.getToken());
			
		} catch(Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		
	
		try
        { 
			String imageName = "JPEG_" + filledId + "_" + "1_"+ questionId + "." + extension;
			//for testing purpose
			File file = new File(uploadDirectoryProperties.getTempFileUploadingDirectory() + imageName);
			Path path = Paths.get(uploadDirectoryProperties.getTempFileUploadingDirectory() + imageName);
			// printing the permissions associated with the file 
            Files.delete(path);
        } 
        catch(NoSuchFileException e) 
        { 
            System.out.println("No such file/directory exists"); 
        } 
        catch(DirectoryNotEmptyException e) 
        { 
            System.out.println("Directory is not empty."); 
        } 
        catch(IOException e) 
        { 
            System.out.println("Invalid permissions."); 
            e.printStackTrace();
        }	
		
		
		return "1";
	}
	
	@PostMapping("/household/edit")
	public @ResponseBody
    int editHouseHold(@RequestBody AnswerDTO answerDto, @AuthenticationPrincipal UserDTO user, HttpServletRequest httpServletRequest) {
        System.out.println(answerDto.toString());
       
        answerDto.setModifiedBy(user.getUserId());
        
        try {
        	surveyAnswerService.editHouseholdSurveyAnswer(answerDto, user.getToken());
        	return 1;
        }catch(Exception e) {
        	e.printStackTrace();
        	log.info(e.getMessage());
        	return 0;
        }
        //return 1;
    }
	
	@RequestMapping(value = "/editImage", method = RequestMethod.POST)
	public @ResponseBody String addEditedImage(MultipartHttpServletRequest request, @AuthenticationPrincipal UserDTO user) {
		
		String inputTagName = request.getParameter("imgIndex");
		System.out.println("InputTagName----> " + inputTagName);
		String extension = request.getParameter("extension");
		String filledId = request.getParameter("filledId");
		String questionId = request.getParameter("questionId");
		Path rootLocation = Paths.get(uploadDirectoryProperties.getTempFileUploadingDirectory());
		try {
			MultipartFile ourImage = request.getFile(inputTagName);
			//ourImage.getOriginalFilename().replace(ourImage.getOriginalFilename(), "JPEG_" + filledId + "_" + questionId + "." + extension);
			
			String imageName = "JPEG_" + filledId + "_" + "1_" + questionId + "." + extension;
			//ourImage.getOriginalFilename().concat(imageName);
			System.out.println("GeneratedImageName----->" + imageName);
			System.out.println(ourImage.getSize());
			System.out.println(ourImage.getOriginalFilename());
			System.out.println("Name----->" + ourImage.getName());
			
			//for copying file to upload directory
			Files.copy(ourImage.getInputStream(), rootLocation.resolve(imageName));
			
			//for retrieving saved multipart file
			File file = new File(uploadDirectoryProperties.getTempFileUploadingDirectory() + imageName);
			FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

			InputStream input = null;
			OutputStream os = null;
			
			try {
			     input = new FileInputStream(file);
			     os = fileItem.getOutputStream();
			    IOUtils.copy(input, os);
			    // Or faster..
			    // IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
			} catch (IOException ex) {
			    // do something.
			}finally {
				if(input != null) {
					input.close();
				}
				if(os!= null) {
					os.close();
				}
			}

			MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
			//ends
			
			surveyAnswerService.addEditedPhoto(multipartFile, imageName, user.getToken());
			
		} catch(Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		
	
		try
        { 
			String imageName = "JPEG_" + filledId + "_" +  "1_" + questionId + "." + extension;
			//for testing purpose
			File file = new File(uploadDirectoryProperties.getTempFileUploadingDirectory() + imageName);
			Path path = Paths.get(uploadDirectoryProperties.getTempFileUploadingDirectory() + imageName);
			// printing the permissions associated with the file 
            Files.delete(path);
        } 
        catch(NoSuchFileException e) 
        { 
            System.out.println("No such file/directory exists"); 
        } 
        catch(DirectoryNotEmptyException e) 
        { 
            System.out.println("Directory is not empty."); 
        } 
        catch(IOException e) 
        { 
            System.out.println("Invalid permissions."); 
            e.printStackTrace();
        }	
		return "1";
	}
}
