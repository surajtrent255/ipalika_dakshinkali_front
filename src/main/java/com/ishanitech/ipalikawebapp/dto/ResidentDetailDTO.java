/**
 * @author Umesh Bhujel <yoomesbhujel@gmail.com>
 * Since Jan 31, 2020
 */
package com.ishanitech.ipalikawebapp.dto;

import java.util.List;
import lombok.Data;

@Data
public class ResidentDetailDTO {
	private Answer residentDetail;
	private List<FamilyMemberDTO> familyMembers;
	private String surveyor;
}
