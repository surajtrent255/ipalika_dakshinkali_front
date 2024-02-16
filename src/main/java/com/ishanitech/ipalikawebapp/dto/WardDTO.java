package com.ishanitech.ipalikawebapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WardDTO {
	private int wardNumber;
	private String wardName;
	private String wardLocation;
	private String wardDescription;
	private String mainPerson;
	private String contactNumber;
	
	private String buildingImage;
}
