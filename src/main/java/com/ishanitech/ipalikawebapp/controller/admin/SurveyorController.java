/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Feb 26, 2020
 */
package com.ishanitech.ipalikawebapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/surveyor")
@Controller
public class SurveyorController {

	@GetMapping
	public String getSurveyorPage() {
		return "private/surveyor/index";
	}
}
