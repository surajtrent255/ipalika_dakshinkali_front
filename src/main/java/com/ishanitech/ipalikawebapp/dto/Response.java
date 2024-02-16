package com.ishanitech.ipalikawebapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Pujan on 1/21/2020.
 *
 * Class used for standardizing response obtained from the web service.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
	private T data;
}
