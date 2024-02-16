package com.ishanitech.ipalikawebapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@code Form} represents the form table.
 * From table holds the information about the survey form.
 * Example: Survey form for the ward residents of municipality.
 * @author Umesh Bhujel, Pujan K.C
 * @since 1.0
 */

@Data
@NoArgsConstructor
public class FormDetail {
	@JsonIgnore
	private int uuid;
	@JsonIgnore
    private int formId;

    private int questionId;
    private String id;
    private String grouping;
    private String desc;
    private String type;
    private Integer isRequired;
    private List<String> options;
}
