package com.ishanitech.ipalikawebapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FamilyMemberDTO {
	private String id;
	private String memberId;
    private String mainId;			//family Id (Form field id)
    private String name;
    private String occupation;
    private String relation;
    private String education;
    private String age;
    private String gender;
    private String maritalStatus;
    private String voterCard;
    private String healthCondition;
    private String dateOfBirthAD;
    private String dateOfBirthBS;

    private String educationalInstitute;
    private String disability;
    
    private Boolean isDead;
    private Boolean submitStatus;
}
