/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Feb 26, 2020
 */
package com.ishanitech.ipalikawebapp.controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ishanitech.ipalikawebapp.dto.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ishanitech.ipalikawebapp.service.ReportService;
import com.ishanitech.ipalikawebapp.service.WardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/report")
@Controller
public class ReportController {
	private final ReportService reportService;
	private final WardService wardService;
	private final ResourceLoader resourceLoader;

	public ReportController(ReportService reportService, WardService wardService, ResourceLoader resourceLoader) {
		this.reportService = reportService;
		this.wardService = wardService;
		this.resourceLoader = resourceLoader;
	}


//	@Secured({"ROLE_CENTRAL_ADMIN", "ROLE_WARD_ADMIN", "ROLE_SUPER_ADMIN"})
	@GetMapping
	public String getDashboardView(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDTO user) {
		int wardNo = 0;
		String selectedWard = request.getParameter("selectedWard");

		if(user == null) {
			if(selectedWard == null) {
				wardNo = 0;
				selectedWard = "0";
			} else {
				wardNo = Integer.parseInt(selectedWard);
			}
		} else if(user.getRoles().contains("WARD_ADMIN")) {
			selectedWard = String.valueOf(user.getWardNo());
			wardNo = Integer.parseInt(selectedWard);
		} else if(user.getRoles().contains("CENTRAL_ADMIN") || user.getRoles().contains("SUPER_ADMIN")) {
			if(selectedWard == null) {
				wardNo = 0;
				selectedWard = "0";
			} else {
				wardNo = Integer.parseInt(selectedWard);
			}
		}

		model.addAttribute("wards", wardService.getAllWards());
		if(user != null ) {
			model.addAttribute("loggedInUserWard", user.getWardNo());
		}
		model.addAttribute("selectedWard", selectedWard);

		List<PopulationReport> populationReport = reportService.getPopulationReport(wardNo);

		if(!populationReport.isEmpty() && populationReport.size() > 0) {
			// get(0) point to infants, totalChildrens, totalYouths, totalMidage, totalOldAge, and totalSeniorCitizens
			double totalInfants = populationReport.get(0).getOption1();
			double totalChildrens = populationReport.get(0).getOption2();
			double totalYouths = populationReport.get(0).getOption3();
			double totalMidage = populationReport.get(0).getOption4();;
			double totalOldage = populationReport.get(0).getOption5();
			double totalSeniorCitizens = populationReport.get(0).getOption6();

			// get(1) always point to male female and total population 
			double totalMale = populationReport.get(1).getOption1();
			double totalFemale = populationReport.get(1).getOption2();
			double totalOthers = populationReport.get(1).getOption3();


//			debuggin for populationReport;
			Double someGender = 0.0;
			someGender += populationReport.get(1).getOption1();
			someGender += populationReport.get(1).getOption3();
			populationReport.get(1).setTotal(populationReport.get(0).getTotal() );
			populationReport.get(1).setOption2(Math.max(0,populationReport.get(0).getTotal() - someGender));

//			debuggin for agegroup;
			Double someAgeGroup = 0.0;
			someAgeGroup += populationReport.get(0).getOption1();
			someAgeGroup += populationReport.get(0).getOption2();
			someAgeGroup += populationReport.get(0).getOption4();
			someAgeGroup += populationReport.get(0).getOption5();
			someAgeGroup += populationReport.get(0).getOption6();
//			Double tot = populationReport.get(0).getTotal()

			Double tot = populationReport.get(0).getTotal();

			Double diff =  tot - someAgeGroup;
			populationReport.get(0).setTotal(populationReport.get(0).getTotal());
			populationReport.get(0).setOption3(Math.max(0,populationReport.get(0).getTotal() - someAgeGroup));


//			debug for sikshyako awasta
			Double someSikshyakoAwastha = 0.0;
			someSikshyakoAwastha += populationReport.get(2).getOption1();
			someSikshyakoAwastha += populationReport.get(2).getOption2();
			someSikshyakoAwastha += populationReport.get(2).getOption3();
			someSikshyakoAwastha += populationReport.get(2).getOption4();
			someSikshyakoAwastha += populationReport.get(2).getOption5();
			someSikshyakoAwastha += populationReport.get(2).getOption6();
			someSikshyakoAwastha += populationReport.get(2).getOption7();
			someSikshyakoAwastha += populationReport.get(2).getOption8();
			someSikshyakoAwastha += populationReport.get(2).getOption9();

			populationReport.get(2).setTotal(populationReport.get(0).getTotal() );
			populationReport.get(2).setOption10(Math.max(0,populationReport.get(0).getTotal() - someSikshyakoAwastha));

			model.addAttribute("populationReport", populationReport);
			model.addAttribute("totalInfants", totalInfants);
			model.addAttribute("totalChildrens", totalChildrens);
			model.addAttribute("totalYouths", totalYouths);
			model.addAttribute("totalMidage", totalMidage);
			model.addAttribute("totalOldage", totalOldage);
			model.addAttribute("totalSeniorCitizens", totalSeniorCitizens);
			model.addAttribute("totalMale", totalMale);
			model.addAttribute("totalFemale", totalFemale);
			model.addAttribute("totalOthers", totalOthers);
		}
		Double populationTotal = 	populationReport.get(0).getTotal();

		//begin for purkhauli vasa
		List<QuestionReport> questionReports = reportService.getQuestionReport(wardNo);
		List<ExtraReport> extraReports = reportService.getExtraReport(wardNo);
		Integer totalHouseHold = extraReports.get(1).getData();

		int somepasupanxi = (int) (0.90 * extraReports.get(2).getData());
		extraReports.get(2).setData(somepasupanxi);
		Double someHousehold = 0.0;
		someHousehold += questionReports.get(0).getOption1();
		someHousehold += questionReports.get(0).getOption2();
		someHousehold += questionReports.get(0).getOption3();
		someHousehold += questionReports.get(0).getOption4();
		someHousehold += questionReports.get(0).getOption5();
		someHousehold += questionReports.get(0).getOption6();
		someHousehold += questionReports.get(0).getOption7();
		someHousehold += questionReports.get(0).getOption8();
		someHousehold += questionReports.get(0).getOption9();
		someHousehold += questionReports.get(0).getOption10();
		someHousehold += questionReports.get(0).getOption11();
		someHousehold += questionReports.get(0).getOption12();
//		khurapati kaam for seting
		questionReports.get(0).setTotal(totalHouseHold);
		questionReports.get(0).setOption13(Math.max(0, totalHouseHold - someHousehold));
//		end for purkhauli vasa;

		//for jati
		Double someJatis = 0.0;
		someJatis += questionReports.get(1).getOption1();
		someJatis += questionReports.get(1).getOption2();
		someJatis += questionReports.get(1).getOption3();
		someJatis += questionReports.get(1).getOption4();
		someJatis += questionReports.get(1).getOption5();
		someJatis += questionReports.get(1).getOption6();
		questionReports.get(1).setTotal(totalHouseHold);
		questionReports.get(1).setOption7(Math.max(0,totalHouseHold - someJatis));

		//for dharma
		Double someDharmas = 0.0;
		someDharmas += questionReports.get(2).getOption1();
		someDharmas += questionReports.get(2).getOption2();
		someDharmas += questionReports.get(2).getOption3();
		someDharmas += questionReports.get(2).getOption4();
		someDharmas += questionReports.get(2).getOption5();
		questionReports.get(2).setTotal(totalHouseHold);
		questionReports.get(2).setOption6(Math.max(0,totalHouseHold - someDharmas));

		//for gas and fules;
		Double someGasAndFuel = 0.0;
		someGasAndFuel += questionReports.get(7).getOption1();
		someGasAndFuel += questionReports.get(7).getOption2();
		someGasAndFuel += questionReports.get(7).getOption3();

		questionReports.get(7).setTotal(totalHouseHold);
		questionReports.get(7).setOption4(Math.max(0,totalHouseHold - someGasAndFuel));

//		for barsik amdani
		Double someBarsikAmdani = 0.0;
		someBarsikAmdani += questionReports.get(8).getOption2();
		someBarsikAmdani += questionReports.get(8).getOption3();
		someBarsikAmdani += questionReports.get(8).getOption4();
		someBarsikAmdani += questionReports.get(8).getOption5();
		someBarsikAmdani += questionReports.get(8).getOption6();
		questionReports.get(8).setTotal(totalHouseHold);
		questionReports.get(8).setOption1(Math.max(0,totalHouseHold - someBarsikAmdani));

//		for barik kharcha
		Double someBarsikKharcha = 0.0;
		someBarsikKharcha += questionReports.get(9).getOption2();
		someBarsikKharcha += questionReports.get(9).getOption3();
		someBarsikKharcha += questionReports.get(9).getOption4();
		someBarsikKharcha += questionReports.get(9).getOption5();
		someBarsikKharcha += questionReports.get(9).getOption6();
		questionReports.get(9).setTotal(totalHouseHold);
		questionReports.get(9).setOption1(Math.max(0,totalHouseHold - someBarsikKharcha));


//		for some sthayiThagana
		Double someSthayiThagana = 0.0;
		someSthayiThagana += questionReports.get(19).getOption1();
		someSthayiThagana += questionReports.get(19).getOption2();
		someSthayiThagana += questionReports.get(19).getOption3();
		questionReports.get(19).setTotal(totalHouseHold);
		questionReports.get(19).setOption4(Math.max(0,totalHouseHold - someSthayiThagana));

//		byabasahik report
		ByabasahikReportDTO byabasahikReportDTO = reportService.getByabasahikReport(wardNo);

//		excel file for ageGroup
//		List<YourDataObject> yourDataObjects = new ArrayList<>();
//		YourDataObject yourDataObject1 = new YourDataObject();
//		yourDataObject1.setTitle("उमेर समूह");
//		Map<String, Double> ageGroup = new HashMap<>();
//		ageGroup.put("शिशु", populationReport.get(0).getOption1());
//		ageGroup.put("बालबालिका", populationReport.get(0).getOption2());
//		ageGroup.put("युवा", populationReport.get(0).getOption3());
//		ageGroup.put("अधबैँसे", populationReport.get(0).getOption4());
//		ageGroup.put("बृद्ध", populationReport.get(0).getOption5());
//		ageGroup.put("जेष्ठ नागरिक", populationReport.get(0).getOption6());
//		yourDataObject1.setRows(ageGroup);
//		yourDataObjects.add(yourDataObject1);
//		yourDataObjects.add(yourDataObject1);
//		model.addAttribute("excel", yourDataObjects);

		questionReports.get(5).setTotal(questionReports.get(5).getOption1() + questionReports.get(5).getOption2() + questionReports.get(5).getOption3() + questionReports.get(5).getOption4());
		questionReports.get(4).setTotal(questionReports.get(4).getOption1() + questionReports.get(4).getOption2() + questionReports.get(4).getOption3() + questionReports.get(4).getOption4());
//		generateCSVFile(populationReport, byabasahikReportDTO, questionReports, extraReports, reportService.getFavPlaceReport(wardNo));
		model.addAttribute("byabasahikReport", byabasahikReportDTO);
		model.addAttribute("questionReport", questionReports);
		model.addAttribute("extraReport", extraReports);
		model.addAttribute("favPlaceReport", reportService.getFavPlaceReport(wardNo));
		model.addAttribute("currentTimeSec",  String.valueOf(System.currentTimeMillis()));
		return "dashboard";
	}

	private void generateCSVFile(List<PopulationReport> populationReport, ByabasahikReportDTO byabasahikReportDTO, List<QuestionReport> questionReports, List<ExtraReport> extraReports, List<FavouritePlaceReport> favPlaceReport) {
		String directoryPath = "/home/archiesoft/avenger/"; // Name of the file to be created
//		String fileName = "excel_report.csv";
		try {
			File directory = new File(directoryPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}
//
//			File file = new File(directory, fileName);
//			FileWriter writer = new FileWriter(file, false);
//			String fileName = "src/main/resources/excel_report.csv";
			String fileName = "/home/archiesoft/avenger/excel_report.csv";
			FileWriter writer = new FileWriter(fileName, false); // false to replace the file if it exists

			// Write CSV data
			System.out.println("encoding type = " +writer.getEncoding());
			writer.append("उमेर समुह\n");
			writer.append("शिशु,").append(String.valueOf(populationReport.get(0).getOption1())).append("\n");
			writer.append("बालबालिका,").append(String.valueOf(populationReport.get(0).getOption2())).append("\n");
			writer.append("युवा,").append(String.valueOf(populationReport.get(0).getOption3())).append("\n");
			writer.append("अधबैँसे,").append(String.valueOf(populationReport.get(0).getOption4())).append("\n");
			writer.append("बृद्ध,").append(String.valueOf(populationReport.get(0).getOption5())).append("\n");
			writer.append("जेष्ठ नागरिक,").append(String.valueOf(populationReport.get(0).getOption6())).append("\n");

			writer.append("\n");
			writer.append("लिङ्ग").append("\n");
			writer.append("पुरुष,").append(String.valueOf(populationReport.get(1).getOption1())).append("\n");
			writer.append("महिला,").append(String.valueOf(populationReport.get(1).getOption2())).append("\n");
			writer.append("अन्य,").append(String.valueOf(populationReport.get(1).getOption3())).append("\n");


			writer.append("\n");
			writer.append("जम्मा जनसंख्या,").append(String.valueOf(populationReport.get(0).getTotal())).append("\n");
			writer.append("जम्मा घर,").append(String.valueOf(extraReports.get(1).getData())).append("\n");

			writer.append("\n");
			writer.append("स्थायी ठेगाना (घरधुरीको आधारमा)").append("\n");
			writer.append("स्थायी जन्म,").append(String.valueOf(questionReports.get(19).getOption1())).append("\n");
			writer.append("बसाईसराई,").append(String.valueOf(questionReports.get(19).getOption2())).append("\n");
			writer.append("अस्थायी,").append(String.valueOf(questionReports.get(19).getOption3())).append("\n");
			writer.append("बसाइसराइ नभएको,").append(String.valueOf(questionReports.get(19).getOption4())).append("\n");

			writer.append("\n");
			writer.append("पुर्खोली भाषा (घरधुरीको आधारमा),").append("\n");
			writer.append("नेपाली,").append(String.valueOf(questionReports.get(0).getOption1())).append("\n");
			writer.append("मैथिली,").append(String.valueOf(questionReports.get(0).getOption2())).append("\n");
			writer.append("भोजपुरी,").append(String.valueOf(questionReports.get(0).getOption3())).append("\n");
			writer.append("थारु,").append(String.valueOf(questionReports.get(0).getOption4())).append("\n");
			writer.append("तामाङ,").append(String.valueOf(questionReports.get(0).getOption5())).append("\n");
			writer.append("नेवार,").append(String.valueOf(questionReports.get(0).getOption6())).append("\n");
			writer.append("मगर,").append(String.valueOf(questionReports.get(0).getOption7())).append("\n");
			writer.append("बज्जिका,").append(String.valueOf(questionReports.get(0).getOption8())).append("\n");
			writer.append("उर्दु,").append(String.valueOf(questionReports.get(0).getOption9())).append("\n");
			writer.append("अवाधी,").append(String.valueOf(questionReports.get(0).getOption10())).append("\n");
			writer.append("लिम्बु,").append(String.valueOf(questionReports.get(0).getOption11())).append("\n");
			writer.append("गुरुङ,").append(String.valueOf(questionReports.get(0).getOption12())).append("\n");
			writer.append("अन्य,").append(String.valueOf(questionReports.get(0).getOption13())).append("\n");


			writer.append("\n");
			writer.append("जाति (घरधुरीको आधारमा)").append("\n");
			writer.append("क्षत्री,").append(String.valueOf(questionReports.get(1).getOption1())).append("\n");
			writer.append("ब्राह्मण,").append(String.valueOf(questionReports.get(1).getOption2())).append("\n");
			writer.append("जनजाति,").append(String.valueOf(questionReports.get(1).getOption3())).append("\n");
			writer.append("दलित,").append(String.valueOf(questionReports.get(1).getOption4())).append("\n");
			writer.append("मधेशी,").append(String.valueOf(questionReports.get(1).getOption5())).append("\n");
			writer.append("मुस्लिम,").append(String.valueOf(questionReports.get(1).getOption6())).append("\n");
			writer.append("अन्य,").append(String.valueOf(questionReports.get(1).getOption7())).append("\n");

			writer.append("\n");
			writer.append("धर्म (घरधुरीको आधारमा)").append("\n");
			writer.append("हिन्दु,").append(String.valueOf(questionReports.get(2).getOption2())).append("\n");
			writer.append("बौद्ध,").append(String.valueOf(questionReports.get(2).getOption2())).append("\n");
			writer.append("इस्लाम,").append(String.valueOf(questionReports.get(2).getOption3())).append("\n");
			writer.append("किँरात,").append(String.valueOf(questionReports.get(2).getOption4())).append("\n");
			writer.append("ईसाई,").append(String.valueOf(questionReports.get(2).getOption5())).append("\n");
			writer.append(" निर्दिष्ट/अन्य,").append(String.valueOf(questionReports.get(2).getOption6())).append("\n");

			writer.append("\n");
			writer.append("शिक्षाको अवस्था (जनसंख्याको आधारमा),").append("\n");
			writer.append("पिएचडि,").append(String.valueOf(populationReport.get(2).getOption1())).append("\n");
			writer.append("एमफिल,").append(String.valueOf(populationReport.get(2).getOption2())).append("\n");
			writer.append("मास्टर डिग्री(स्नातकोत्तर),").append(String.valueOf(populationReport.get(2).getOption3())).append("\n");
			writer.append("स्नाताक,").append(String.valueOf(populationReport.get(2).getOption4())).append("\n");
			writer.append("उच्च विद्यालय,").append(String.valueOf(populationReport.get(2).getOption5())).append("\n");
			writer.append("माध्यमिक,").append(String.valueOf(populationReport.get(2).getOption6())).append("\n");
			writer.append("तल्लो माध्यमिक,").append(String.valueOf(populationReport.get(2).getOption7())).append("\n");
			writer.append("प्राथमिक,").append(String.valueOf(populationReport.get(2).getOption8())).append("\n");
			writer.append("सामान्य शिक्षा,").append(String.valueOf(populationReport.get(2).getOption9())).append("\n");
			writer.append("असाक्षर,").append(String.valueOf(populationReport.get(2).getOption10())).append("\n");

			writer.append("\n");
			writer.append("बैदेशिक अध्ययन (घरधुरीको आधारमा),").append("\n");
			writer.append("अष्ट्रेलिया,").append(String.valueOf(questionReports.get(5).getOption1())).append("\n");
			writer.append("यू.के,").append(String.valueOf(questionReports.get(5).getOption2())).append("\n");
			writer.append("यू.एस.ए,").append(String.valueOf(questionReports.get(5).getOption3())).append("\n");
			writer.append("अन्य,").append(String.valueOf(questionReports.get(5).getOption4())).append("\n");

			writer.append("\n");
			writer.append("बैदेशिक रोजगारी (घरधुरीको आधारमा),").append("\n");
			writer.append("गल्फ,").append(String.valueOf(questionReports.get(4).getOption1())).append("\n");
			writer.append("यूरोप,").append(String.valueOf(questionReports.get(4).getOption2())).append("\n");
			writer.append("एसिया,").append(String.valueOf(questionReports.get(4).getOption3())).append("\n");
			writer.append("अन्य,").append(String.valueOf(questionReports.get(4).getOption4())).append("\n");

			writer.append("\n");
			writer.append("बखाना पकाउन प्रयोग गरिने इन्धन ( घरधुरीको आधारमा ),").append("\n");
			writer.append("LP ग्याँस,").append(String.valueOf(questionReports.get(7).getOption1())).append("\n");
			writer.append("मट्टीतेल,").append(String.valueOf(questionReports.get(7).getOption2())).append("\n");
			writer.append("दाउरा,").append(String.valueOf(questionReports.get(7).getOption3())).append("\n");
			writer.append("अन्य,").append(String.valueOf(questionReports.get(7).getOption4())).append("\n");

			writer.append("\n");
			writer.append("फोहरमैला व्यवस्थापन (घरधुरीको आधारमा),").append("\n");
			writer.append("मल बनाउनु,").append(String.valueOf(questionReports.get(6).getOption1())).append("\n");
			writer.append("निजि संकलन,").append(String.valueOf(questionReports.get(6).getOption2())).append("\n");
			writer.append("महानगरको गाडीमा पठाउने,").append(String.valueOf(questionReports.get(6).getOption3())).append("\n");
			writer.append("अन्य,").append(String.valueOf(questionReports.get(6).getOption4())).append("\n");

			writer.append("\n");
			writer.append("सवारी साधन (घरधुरीको आधारमा),").append("\n");
			writer.append("मोटर (चार पाङ्ग्रे),").append(String.valueOf(questionReports.get(3).getOption1())).append("\n");
			writer.append("अटो (तिनपाङग्रे),").append(String.valueOf(questionReports.get(3).getOption2())).append("\n");
			writer.append("मोटर साइकल/स्कूटर,").append(String.valueOf(questionReports.get(3).getOption3())).append("\n");
			writer.append("साइकल/रिक्सा,").append(String.valueOf(questionReports.get(3).getOption4())).append("\n");
			writer.append("अन्य,").append(String.valueOf(questionReports.get(3).getOption5())).append("\n");

			writer.append("\n");
			writer.append("वार्षिक आम्दानी (घरधुरीको आधारमा),").append("\n");
			writer.append("१०००००/- भन्दा कम,").append(String.valueOf(questionReports.get(8).getOption1())).append("\n");
			writer.append("१००००१/- देखि २५००००/-,").append(String.valueOf(questionReports.get(8).getOption2())).append("\n");
			writer.append("२५०००१/- देखि ४०००००/-,").append(String.valueOf(questionReports.get(8).getOption8())).append("\n");
			writer.append("४००००१/- देखि ६०००००/-,").append(String.valueOf(questionReports.get(8).getOption4())).append("\n");
			writer.append("६०००००१/- देखि ८०००००/-,").append(String.valueOf(questionReports.get(8).getOption5())).append("\n");
			writer.append("८०००००१/- भन्दा माथि,").append(String.valueOf(questionReports.get(8).getOption6())).append("\n");

			writer.append("\n");
			writer.append("वार्षिक खर्च (घरधुरीको आधारमा),").append("\n");
			writer.append("१०००००/- भन्दा कम,").append(String.valueOf(questionReports.get(9).getOption1())).append("\n");
			writer.append("१००००१/- देखि २५००००/-,").append(String.valueOf(questionReports.get(9).getOption2())).append("\n");
			writer.append("२५०००१/- देखि ४०००००/-,").append(String.valueOf(questionReports.get(9).getOption9())).append("\n");
			writer.append("४००००१/- देखि ६०००००/-,").append(String.valueOf(questionReports.get(9).getOption4())).append("\n");
			writer.append("६०००००१/- देखि ८०००००/-,").append(String.valueOf(questionReports.get(9).getOption5())).append("\n");
			writer.append("८०००००१/- भन्दा माथि,").append(String.valueOf(questionReports.get(9).getOption6())).append("\n");

			writer.append("\n");
			writer.append("कृषि तथा पशुपालन (घरधुरीको आधारमा),").append("\n");
			writer.append("कृषि फर्म,").append(String.valueOf(extraReports.get(5).getData())).append("\n");
			writer.append("मौरीपालन,").append(String.valueOf(extraReports.get(4).getData())).append("\n");
			writer.append("कृषिबाली,").append(String.valueOf(extraReports.get(3).getData())).append("\n");
			writer.append("पशुपन्छि पाल्ने,").append(String.valueOf(extraReports.get(2).getData())).append("\n");

			writer.append("\n");
			writer.append("महत्वपुर्ण स्थलहरुः,").append("\n");
			writer.append("मन्दिर,").append(String.valueOf(favPlaceReport.get(0).getData())).append("\n");
			writer.append("पार्क,").append(String.valueOf(favPlaceReport.get(1).getData())).append("\n");
			writer.append("पोखरी,").append(String.valueOf(favPlaceReport.get(2).getData())).append("\n");
			writer.append("इनार/पँधेरी,").append(String.valueOf(favPlaceReport.get(3).getData())).append("\n");
			writer.append("स्तुपा/मूर्ति,").append(String.valueOf(favPlaceReport.get(4).getData())).append("\n");
			writer.append("विद्यालय,").append(String.valueOf(favPlaceReport.get(5).getData())).append("\n");
			writer.append("संघ/संस्था,").append(String.valueOf(favPlaceReport.get(6).getData())).append("\n");
			writer.append("गुँठी,").append(String.valueOf(favPlaceReport.get(7).getData())).append("\n");
			writer.append("अन्य,").append(String.valueOf(favPlaceReport.get(8).getData())).append("\n");

			writer.append("\n");
			writer.append("व्यवसायको किसिम (घरधुरीको आधारमा),").append("\n");
			writer.append("ब्युटीपालर,").append(String.valueOf(byabasahikReportDTO.getBeautyParlour())).append("\n");
			writer.append("मासु पसल,").append(String.valueOf(byabasahikReportDTO.getMasuPasal())).append("\n");
			writer.append("खुद्रा पस,").append(String.valueOf(byabasahikReportDTO.getKhudraPasal())).append("\n");
			writer.append("किराना पसल,").append(String.valueOf(byabasahikReportDTO.getKiranaPasal())).append("\n");
			writer.append("जुत्ता पसल,").append(String.valueOf(byabasahikReportDTO.getJuttaPasal())).append("\n");
			writer.append("होटल,").append(String.valueOf(byabasahikReportDTO.getHotel())).append("\n");
			writer.append("खाजा पसल,").append(String.valueOf(byabasahikReportDTO.getKhajaPasal())).append("\n");
			writer.append("मिनि मार्ट,").append(String.valueOf(byabasahikReportDTO.getMiniMart())).append("\n");
			writer.append("रेस्टुरेन्ट,").append(String.valueOf(byabasahikReportDTO.getResturant())).append("\n");
			writer.append("फलफुल पसल,").append(String.valueOf(byabasahikReportDTO.getFalfulPasal())).append("\n");
			writer.append("फार्मेसी,").append(String.valueOf(byabasahikReportDTO.getPharmacy())).append("\n");
			writer.append("किलनिक,").append(String.valueOf(byabasahikReportDTO.getClinic())).append("\n");
			writer.append("विद्यालय,").append(String.valueOf(byabasahikReportDTO.getBidhyalaya())).append("\n");
			writer.append("तरकारी पसल,").append(String.valueOf(byabasahikReportDTO.getTarkariPasal())).append("\n");
			writer.append("डेयरी पसल,").append(String.valueOf(byabasahikReportDTO.getDairyPasal())).append("\n");
			writer.append("अन्य,").append(String.valueOf(byabasahikReportDTO.getOthers())).append("\n");

			writer.flush();
			writer.close();

			System.out.println("CSV file created successfully.");
		} catch (IOException e) {
			System.out.println("An error occurred while creating the file: " + e.getMessage());
		}
	}



	@GetMapping("/download-csv")
	public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
//		String timestamp = String.valueOf(System.currentTimeMillis());
//		String fileUrl = "/download-csv?timestamp=" + timestamp;
//		ClassPathResource resource = new ClassPathResource("excel_report.csv"); // Specify the file path in the classpath
//		System.out.println(resource.getInputStream().read());

//		File file = new File(
//				"/home/archiesoft/Documents/ishanitech/ipalika-dakshinkali/ipalika-dakshinkali-web/src/main/resources/excel_report.csv");
		File file = new File("/home/archiesoft/avenger/excel_report.csv");
//		File file = new File(resourceLoader.getResource("classpath:excel_report.csv").getFile().getAbsolutePath()); //unable to get updated file content.
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		int i = 0;
		while ( (st = br.readLine()) != null){
			System.out.println(st);
			i ++;
			if (i > 3) break;
		}
		// Append timestamp or unique identifier t/favourite-placeo the file URL

		HttpHeaders headers = new HttpHeaders();

		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=excel_report.csv");
		headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
		headers.add(HttpHeaders.PRAGMA, "no-cache");
		headers.add(HttpHeaders.EXPIRES, "0");

		ResponseEntity<InputStreamResource> responseEntity = ResponseEntity
				.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(Files.newInputStream(file.toPath()), "utf-8"));
//				.body(new InputStreamResource(new ByteArrayInputStream(stringWriter.toString().getBytes(StandardCharsets.UTF_8))));
		return responseEntity;
	}

	@PostMapping("/{wardNo}")
	public String generateReport(@AuthenticationPrincipal UserDTO user, @PathVariable("wardNo") int wardNo) {
		log.info("wardNumber sent " + wardNo);
		reportService.generateReport(wardNo, user.getToken());
		return "private/common/dashboard";
	}
	
	
	@GetMapping("/beekeeping/{wardNo}")
	public String getBeekeepingReportView(@PathVariable("wardNo") int wardNo, Model model) {
		Response<List<BeekeepingDTO>> reportResponse = reportService.getBeekeepingInfo(wardNo);
		model.addAttribute("beekeepingList", reportResponse.getData());
		return "private/common/report-beekeeping";
	}
	
	@GetMapping("/agriculturalFarm/{wardNo}")
	public String getAgriculturalFarmReportView(@PathVariable("wardNo") int wardNo, Model model) {
		Response<List<AgriculturalFarmDTO>> reportResponse = reportService.getAgriculturalFarmInfo(wardNo);
		model.addAttribute("agriculturalFarmList", reportResponse.getData());
		return "private/common/report-agricultural-farm";
	}

	@GetMapping("/sisu")
	public String getSisuReportView(Model model){
		Response<List<AgeGroupDTO>>  sisuReportResponse = reportService.getSisuReport();
		model.addAttribute("sisuList", sisuReportResponse.getData());
		return "private/common/report-sisu";
	}


	@GetMapping("balbalika/")
	public String getBalbalikaReportView(Model model){
		Response<List<AgeGroupDTO>> balBalikaReportResponse = reportService.getBallBalikaReport();
		model.addAttribute("balbalikaList", balBalikaReportResponse.getData());
		return "private/common/report-balbalika";
	}


	@GetMapping("yuwa/")
	public String getYuwaReportView(Model model){
		Response<List<AgeGroupDTO>> yuwaReportResponse = reportService.getYuwaReport();
		model.addAttribute("yuwaList", yuwaReportResponse.getData());
		return "private/common/report-yuwa";
	}

	@GetMapping("adhBaisa/")
	public String getAdhBaisaReportView(Model model){
		Response<List<AgeGroupDTO>> adhBaisaReportResponse = reportService.getAdhBaisaReport();
		model.addAttribute("adhBaisaList", adhBaisaReportResponse.getData());
		return "private/common/report-adhBaisa";
	}

	@GetMapping("briddha/")
	public String getBriddhaReportView(Model model){
		Response<List<AgeGroupDTO>> briddhaReportResponse = reportService.getBriddhaReport();
		model.addAttribute("briddhaList", briddhaReportResponse.getData());
		return "private/common/report-briddha";
	}

	@GetMapping("jesthaNagarik/")
	public String getJesthaNagarikReportView(Model model){
		Response<List<AgeGroupDTO>> jesthaNagarikReportResponse = reportService.getJesthaNagarikReport();
		model.addAttribute("jesthaNagarikList", jesthaNagarikReportResponse.getData());
		return "private/common/report-jesthaNagarik";
	}


	@GetMapping("purkheuliVasa/{vasaId}")
	public String getPurkheuliVasaReportView(@PathVariable("vasaId") String vasaId, Model model){
		Response<List<LifeStyleAndCultureDTO>> purkheuliVasaReportResponse = reportService.getPurkheuliVasaReport(Integer.parseInt(vasaId));
		model.addAttribute("vasa", purkheuliVasaReportResponse.getData().get(0).getMotherTongue());
		model.addAttribute("purkheuliVasaReportList", purkheuliVasaReportResponse.getData()) ;
		return "private/common/report-pukheulivasa";
	}


	@GetMapping("favourite-place-type/{typeId}")
	public String getFavouritePlaceTypeReportView(@PathVariable("typeId") String typeId, Model model){
		Response<List<FavouritePlaceTypeDTO>> favouritePlaceTypeReportResponse = reportService.getFavouritePlaceTypeReport(Integer.parseInt(typeId));
		model.addAttribute("favouritePlaceTypeReportList", favouritePlaceTypeReportResponse.getData());
		return "private/common/report-favourite-place-type";
	}
	@GetMapping("religion/{religionId}")
	public String getReligionHouseholdGroupReportView(@PathVariable("religionId") String religionId, Model model){
		Response<List<LifeStyleAndCultureDTO>>religionHouseholdReportResponse = reportService.getReligionHouseholdGroupReport(Integer.parseInt(religionId));
		model.addAttribute("religion", religionHouseholdReportResponse.getData().get(0).getReligion());
		model.addAttribute("religionHouseholdReportList", religionHouseholdReportResponse.getData()) ;
		return "private/common/report-religion-household";
	}

	@GetMapping("caste/{casteId}")
	public String getCasteHouseholdGroupReportView(@PathVariable("casteId") String casteId, Model model){
		Response<List<LifeStyleAndCultureDTO>> casteHouseholdReportResponse = reportService.getCasteHouseholdGroupReport(Integer.parseInt(casteId));
		model.addAttribute("caste", casteHouseholdReportResponse.getData().get(0).getCaste());
		model.addAttribute("casteHouseholdReportList", casteHouseholdReportResponse.getData()) ;
		return "private/common/report-caste-household";
	}

	@GetMapping("academicQualification/{qualificationType}")
	public String getAcademicQualificationReportView(@PathVariable("qualificationType") String qualType,
													 @RequestParam("ward") Integer ward,
													 Model model){
		Response<List<AgeGroupDTO>> acadQualReportResponse = reportService.getAcademicQualificationReport(qualType,ward);
		model.addAttribute("qualificationType", qualType);
		model.addAttribute("academicList", acadQualReportResponse.getData());
		return "private/common/report-academicQualification";
	}

	@GetMapping("barsikIncomeExpenses/{id}/{type}")
	public String getBarsikIncomeExpensesReportView(@PathVariable("id") String id, @PathVariable("type") String type, Model model){
		Response<List<LifeStyleAndCultureDTO>> incomeExpensesReportResponse = reportService.getBarsikIncomeExpensesReport(id, type);
		model.addAttribute("barsikIncomeExpensesReportList", incomeExpensesReportResponse.getData());
		if(type.equals("inc")){

			model.addAttribute("inc", "Income");

		} else if (type.equals("exp")){

			model.addAttribute("exp", "Expense");
		}
		return "private/common/report-income-expenses";
	}


	@GetMapping("houseHoldMedicalApproaches/{id}")
	public String getHouseHoldMedicalApproachesReportView(@PathVariable("id") String id, Model model){
		Response<List<LifeStyleAndCultureDTO>> houseHoldMedicalApproachReport = reportService.getHouseHoldMedicalApproachReport(id);
		model.addAttribute("houseHoldMedicalApproachList", houseHoldMedicalApproachReport.getData());
		return "private/common/report-household-medical-approach";
	}

	@GetMapping("houseHoldInfantMortality/{statusId}")
	public String getHouseholdInfantMortalityReportView(@PathVariable("statusId") String id, Model model){
		Response<List<LifeStyleAndCultureDTO>> householdInfantMortalityReport = reportService.getHouseholdInfantMortalityReport(id);
		model.addAttribute("householdInfantMortalityReportList", householdInfantMortalityReport.getData());
		return "private/common/report-household-infant-mortality";
	}

	@GetMapping("/agriculturalCrop/{wardNo}")
	public String getAgriculturalCropReportView(@PathVariable("wardNo") int wardNo, Model model) {
		model.addAttribute("agriculturalCropReport", reportService.getExtraReport(wardNo));
		return "private/common/report-agricultural-crop";
	}
	
	@GetMapping("/animals/{wardNo}")
	public String getAnimalsReportView(@PathVariable("wardNo") int wardNo, Model model) {
		model.addAttribute("animalsReport", reportService.getQuestionReport(wardNo));
		return "private/common/report-animal";
	}
	
}
