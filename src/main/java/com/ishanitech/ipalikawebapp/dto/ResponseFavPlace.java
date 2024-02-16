package com.ishanitech.ipalikawebapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFavPlace<T> {
    private T data;
    private List<FavouritePlaceDTO> wardFavPlaces;
}
