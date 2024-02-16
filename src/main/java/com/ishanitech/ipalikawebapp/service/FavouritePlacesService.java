package com.ishanitech.ipalikawebapp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ishanitech.ipalikawebapp.dto.ResponseFavPlace;
import org.springframework.web.multipart.MultipartFile;

import com.ishanitech.ipalikawebapp.dto.FavouritePlaceDTO;
import com.ishanitech.ipalikawebapp.dto.Response;

public interface FavouritePlacesService {

	ResponseFavPlace<List<FavouritePlaceDTO>> getAllFavouritePlaces();
	
	Response<List<FavouritePlaceDTO>> getAllFavouritePlacesBySurveyor(int userId);

	Response<FavouritePlaceDTO> getFavouritePlaceByPlaceId(String placeId);

	void deleteFavouritePlacebyPlaceId(String favPlaceId, String token);

	void addFavouritePlaceInfo(FavouritePlaceDTO favouritePlaceInfo, String token);

	void addFavouritePlaceImage(MultipartFile file, String imageName, String token);

	List<String> getTypesofFavourtiePlaces();

	void editFavouritePlaceInfo(FavouritePlaceDTO favPlaceDTO, String favPlaceId, String token);

	void addEditedFavouritePlaceImage(MultipartFile file, String imageName, String token);

	ResponseFavPlace<List<FavouritePlaceDTO>> searchFavouritePlaceByWard(HttpServletRequest request, String wardNo);

	List<FavouritePlaceDTO> searchFavouritePlaceByKey(HttpServletRequest request, String searchKey, String wardNo, String placeType);

	List<FavouritePlaceDTO> searchFavouritePlaceByPlaceType(HttpServletRequest request, String placeType);

	List<FavouritePlaceDTO> getFavouritePlaceByPageLimit(HttpServletRequest request, String wardNo);

	List<FavouritePlaceDTO> getNextLotFavouritePlace(HttpServletRequest request);

	List<FavouritePlaceDTO> getSortedFavouritePlace(HttpServletRequest request);

	List<FavouritePlaceDTO> searchFavouritePlaceBySurveyor(HttpServletRequest request, String surveyor);

	List<FavouritePlaceDTO> getFavouritePlaceForGoBack(HttpServletRequest request);
}
