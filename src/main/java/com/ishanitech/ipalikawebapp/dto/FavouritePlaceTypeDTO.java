package com.ishanitech.ipalikawebapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouritePlaceTypeDTO {
    private int id;
    private String filledId;
    private String placeName;
    private String placeDescription;
    private String placeImage;
    private String placeWard;
    private String placeGPS;
    private String placeType;
    private Boolean submitStatus;
    private String addedBy;
    private String surveyor;
}
