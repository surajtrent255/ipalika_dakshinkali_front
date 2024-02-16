package com.ishanitech.ipalikawebapp.serviceImpl;

import java.util.Collections;
import java.util.List;

import com.ishanitech.ipalikawebapp.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ishanitech.ipalikawebapp.configs.properties.RestApiProperties;
import com.ishanitech.ipalikawebapp.service.ReportService;
import com.ishanitech.ipalikawebapp.utilities.HttpUtils;

import javax.print.attribute.standard.Media;

@Service
public class ReportServiceImpl implements ReportService {
	private final RestTemplate restTemplate;
	private final RestApiProperties restApiProperties;
	private final String REPORT_BASE = "report/";

	public ReportServiceImpl(RestTemplate restTemplate, RestApiProperties restApiProperties) {
		this.restTemplate = restTemplate;
		this.restApiProperties = restApiProperties;
	}

	@Override
	public List<PopulationReport> getPopulationReport(int wardNo) {
		String template =  REPORT_BASE + "{populationReport}/" + wardNo;
		String url = HttpUtils.createRequestUrl(restApiProperties, template, Collections.singletonMap("populationReport", "population"));
		RequestEntity request = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<PopulationReport>>> bean = new ParameterizedTypeReference<Response<List<PopulationReport>>>() {};
		return restTemplate.exchange(request, bean).getBody().getData();
	}

	@Override
	public List<QuestionReport> getQuestionReport(int wardNo) {
		String template =  REPORT_BASE + "{questionReport}/" + wardNo;
		String url = HttpUtils.createRequestUrl(restApiProperties, template, Collections.singletonMap("questionReport", "question"));
		RequestEntity request = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<QuestionReport>>> bean = new ParameterizedTypeReference<Response<List<QuestionReport>>>() {};
		return restTemplate.exchange(request, bean).getBody().getData();
	}

	@Override
	public void generateReport(int wardNo, String token) {
		String template = REPORT_BASE + "/" + wardNo;
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity requestEntity = HttpUtils.createRequestEntity(HttpMethod.POST, MediaType.APPLICATION_JSON, token, url);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {};
		restTemplate.exchange(requestEntity, responseType);
	}

	@Override
	public List<ExtraReport> getExtraReport(int wardNo) {
		String template =  REPORT_BASE + "{extraReport}/" + wardNo;
		String url = HttpUtils.createRequestUrl(restApiProperties, template, Collections.singletonMap("extraReport", "extra"));
		RequestEntity request = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<ExtraReport>>> bean = new ParameterizedTypeReference<Response<List<ExtraReport>>>() {};
		return restTemplate.exchange(request, bean).getBody().getData();
	}

	@Override
	public Response<List<BeekeepingDTO>> getBeekeepingInfo(int wardNo) {
		String template = String.format("%s/%s", REPORT_BASE, "beekeeping/" + wardNo);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<BeekeepingDTO>>> responseType = new ParameterizedTypeReference<Response<List<BeekeepingDTO>>>() {};
		Response<List<BeekeepingDTO>> beekeepingInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return beekeepingInfo;
	}

	@Override
	public Response<List<AgriculturalFarmDTO>> getAgriculturalFarmInfo(int wardNo) {
		String template = String.format("%s/%s", REPORT_BASE, "agriculturalFarm/" +  wardNo);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<AgriculturalFarmDTO>>> responseType = new ParameterizedTypeReference<Response<List<AgriculturalFarmDTO>>>() {};
		Response<List<AgriculturalFarmDTO>> agriculturalFarmInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return agriculturalFarmInfo;
	}

	@Override
	public Response<List<AgeGroupDTO>> getSisuReport() {
		String template = String.format("%s/%s", REPORT_BASE, "sisu/");
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<AgeGroupDTO>>>  responseType = new ParameterizedTypeReference<Response<List<AgeGroupDTO>>>() {};
		Response<List<AgeGroupDTO>> sisuInfo =restTemplate.exchange(requestEntity, responseType).getBody();
		return sisuInfo;
	}

	@Override
	public Response<List<AgeGroupDTO>> getBallBalikaReport() {
		String template = String.format("%s/%s", REPORT_BASE, "balbalika/");
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<AgeGroupDTO>>> responseType = new ParameterizedTypeReference<Response<List<AgeGroupDTO>>>() {};
		Response<List<AgeGroupDTO>> balbalikaInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return balbalikaInfo;
	}

	@Override
	public Response<List<AgeGroupDTO>> getYuwaReport() {
		String template = String.format("%s/%s", REPORT_BASE, "yuwa/");
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<AgeGroupDTO>>> responseType = new ParameterizedTypeReference<Response<List<AgeGroupDTO>>>() {};
		Response<List<AgeGroupDTO>> yuwaReportInfo  = restTemplate.exchange(requestEntity, responseType).getBody();
		return yuwaReportInfo;
	}

	@Override
	public Response<List<AgeGroupDTO>> getAdhBaisaReport() {
		String template = String.format("%s/%s", REPORT_BASE, "adhBaisa/");
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<AgeGroupDTO>>> responseType = new ParameterizedTypeReference<Response<List<AgeGroupDTO>>>() {};
		Response<List<AgeGroupDTO>> adhBaisaReportInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return adhBaisaReportInfo;
	}

	@Override
	public Response<List<AgeGroupDTO>> getBriddhaReport() {
		String template = String.format("%s/%s", REPORT_BASE, "briddha/");
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<AgeGroupDTO>>> responseType = new ParameterizedTypeReference<Response<List<AgeGroupDTO>>>() {};
		Response<List<AgeGroupDTO>> briddhaReportInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return briddhaReportInfo;
	}

	@Override
	public Response<List<AgeGroupDTO>> getJesthaNagarikReport() {
		String template= String.format("%s/%s", REPORT_BASE, "jesthaNagarik/");
		String url  = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<AgeGroupDTO>>> responseType = new ParameterizedTypeReference<Response<List<AgeGroupDTO>>>() {};
		Response<List<AgeGroupDTO>> jesthaNagarikReportInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return jesthaNagarikReportInfo;
	}

	@Override
	public Response<List<AgeGroupDTO>> getAcademicQualificationReport(String qualType, Integer ward) {
		String template = String.format("%s/%s/%s", REPORT_BASE,"academicQualification", qualType +"?ward="+ward );
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<AgeGroupDTO>>> responseType = new ParameterizedTypeReference<Response<List<AgeGroupDTO>>>() {
		};
		Response<List<AgeGroupDTO>> acadQualReportInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return acadQualReportInfo;
	}

	@Override
	public Response<List<LifeStyleAndCultureDTO>> getReligionHouseholdGroupReport(Integer religionId) {
		String template = String.format("%s/%s/%s", REPORT_BASE,"religionGroup", religionId + "/");
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>> responseType = new ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>>() {
		};
		Response<List<LifeStyleAndCultureDTO>> religionHouseholdGroupReportInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return religionHouseholdGroupReportInfo;
	}

	@Override
	public Response<List<LifeStyleAndCultureDTO>> getCasteHouseholdGroupReport(Integer casteId) {
		String template = String.format("%s/%s/%s", REPORT_BASE,"casteGroup", casteId + "/");
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>> responseType = new ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>>() {
		};
		Response<List<LifeStyleAndCultureDTO>> casteHouseholdGroupReportInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return casteHouseholdGroupReportInfo;
	}

	@Override
	public Response<List<LifeStyleAndCultureDTO>> getPurkheuliVasaReport(Integer vasaId) {
		String template = String.format("%s/%s/%s", REPORT_BASE,"motherTongueGroup", vasaId + "/");
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>> responseType = new ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>>() {
		};
		Response<List<LifeStyleAndCultureDTO>> purkheuliVasaReportInfo = restTemplate.exchange(requestEntity, responseType).getBody();
		return purkheuliVasaReportInfo;
	}

	@Override
	public Response<List<LifeStyleAndCultureDTO>> getBarsikIncomeExpensesReport(String id, String type) {
		String template = String.format("%s/%s/%s/%s/", REPORT_BASE, "annualIncomeExpenses", id,type);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>> responseType = new ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>>(){};
		Response<List<LifeStyleAndCultureDTO>> barsikIncomeExpensesReport = restTemplate.exchange(requestEntity, responseType).getBody();
		return barsikIncomeExpensesReport;
	}

	@Override
	public Response<List<LifeStyleAndCultureDTO>> getHouseHoldMedicalApproachReport(String id) {
		String template = String.format("%s/%s/%s", REPORT_BASE, "houseHoldsMedicalApproach", id);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>> responseType = new ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>>(){};
		Response<List<LifeStyleAndCultureDTO>> houseHoldMedicalApproachReport = restTemplate.exchange(requestEntity, responseType).getBody();
		return houseHoldMedicalApproachReport;
	}

	@Override
	public Response<List<LifeStyleAndCultureDTO>> getHouseholdInfantMortalityReport(String id) {
		String template = String.format("%s/%s/%s", REPORT_BASE, "houseHoldInfantMortality", id);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>> responseType = new ParameterizedTypeReference<Response<List<LifeStyleAndCultureDTO>>>(){};
		Response<List<LifeStyleAndCultureDTO>> householdInfantMortalityReport = restTemplate.exchange(requestEntity, responseType).getBody();
		return householdInfantMortalityReport;
	}

	@Override
	public Response<List<FavouritePlaceTypeDTO>> getFavouritePlaceTypeReport(int typeId) {
		String template = String.format("%s/%s/%s", REPORT_BASE, "favourite-place-type", typeId);
		String url = HttpUtils.createRequestUrl(restApiProperties, template, null);
		RequestEntity<?> requestEntity = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<FavouritePlaceTypeDTO>>> responseType = new ParameterizedTypeReference<Response<List<FavouritePlaceTypeDTO>>>(){};
		Response<List<FavouritePlaceTypeDTO>> householdInfantMortalityReport = restTemplate.exchange(requestEntity, responseType).getBody();
		return householdInfantMortalityReport;
	}

	@Override
	public ByabasahikReportDTO getByabasahikReport(int wardNo) {
		String template =  REPORT_BASE + "byabashahi/" + wardNo;
		String url = HttpUtils.createRequestUrl(restApiProperties, template, Collections.singletonMap("favPlaceReport", "favouritePlace"));
		RequestEntity request = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<ByabasahikReportDTO>> bean = new ParameterizedTypeReference<Response<ByabasahikReportDTO>>() {};
		return restTemplate.exchange(request, bean).getBody().getData();
	}

	@Override
	public List<FavouritePlaceReport> getFavPlaceReport(int wardNo) {
		String template =  REPORT_BASE + "{favPlaceReport}/" + wardNo;
		String url = HttpUtils.createRequestUrl(restApiProperties, template, Collections.singletonMap("favPlaceReport", "favouritePlace"));
		RequestEntity request = HttpUtils.createRequestEntity(HttpMethod.GET, MediaType.APPLICATION_JSON, url);
		ParameterizedTypeReference<Response<List<FavouritePlaceReport>>> bean = new ParameterizedTypeReference<Response<List<FavouritePlaceReport>>>() {};
		return restTemplate.exchange(request, bean).getBody().getData();
	}
	
}
