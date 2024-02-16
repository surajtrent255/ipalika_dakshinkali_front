package com.ishanitech.ipalikawebapp.dto;

import lombok.Data;

@Data
public class FavouritePlaceDTO {

	private int id;
	private String filledId;
    private String placeName;
    private String placeDescription;
    private String placeImage;
    private String placeWard;
    private String placeGPS;
    private String placeType;
    private Boolean submitStatus;
    private int addedBy;
    private String surveyor;
}