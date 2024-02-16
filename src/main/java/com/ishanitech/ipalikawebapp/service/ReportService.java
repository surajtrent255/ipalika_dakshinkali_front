package com.ishanitech.ipalikawebapp.service;

import java.util.List;

import com.ishanitech.ipalikawebapp.dto.*;

public interface ReportService {
	List<PopulationReport> getPopulationReport(int wardNo);
	List<QuestionReport> getQuestionReport(int wardNo);
	List<FavouritePlaceReport> getFavPlaceReport(int wardNo);
	void generateReport(int wardNo, String token);
	List<ExtraReport> getExtraReport(int wardNo);
	Response<List<BeekeepingDTO>> getBeekeepingInfo(int wardNo);
	Response<List<AgriculturalFarmDTO>> getAgriculturalFarmInfo(int wardNo);
	Response<List<AgeGroupDTO>> getSisuReport();
    Response<List<AgeGroupDTO>> getBallBalikaReport();
	Response<List<AgeGroupDTO>> getYuwaReport();
	Response<List<AgeGroupDTO>> getAdhBaisaReport();
	Response<List<AgeGroupDTO>> getBriddhaReport();
	Response<List<AgeGroupDTO>> getJesthaNagarikReport();

    Response<List<AgeGroupDTO>> getAcademicQualificationReport(String qualType, Integer ward);

    Response<List<LifeStyleAndCultureDTO>> getReligionHouseholdGroupReport(Integer religionId);

	Response<List<LifeStyleAndCultureDTO>> getCasteHouseholdGroupReport(Integer casteId);

	Response<List<LifeStyleAndCultureDTO>> getPurkheuliVasaReport(Integer vasaId);

	Response<List<LifeStyleAndCultureDTO>> getBarsikIncomeExpensesReport(String id, String type);

    Response<List<LifeStyleAndCultureDTO>> getHouseHoldMedicalApproachReport(String id);

    Response<List<LifeStyleAndCultureDTO>> getHouseholdInfantMortalityReport(String id);

	Response<List<FavouritePlaceTypeDTO>> getFavouritePlaceTypeReport(int typeId);

    ByabasahikReportDTO getByabasahikReport(int wardNo);
}
