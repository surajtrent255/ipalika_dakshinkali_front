package com.ishanitech.ipalikawebapp.service;

import org.springframework.web.multipart.MultipartFile;

import com.ishanitech.ipalikawebapp.dto.AnswerDTO;

public interface SurveyAnswerService {

	void addHouseholdSurveyAnswer(AnswerDTO answerDto, String token);
	
	void addPhoto(MultipartFile file, String imageName, String token);

	void editHouseholdSurveyAnswer(AnswerDTO answerDto, String token);

	void addEditedPhoto(MultipartFile multipartFile, String imageName, String token);

}
