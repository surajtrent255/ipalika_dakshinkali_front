package com.ishanitech.ipalikawebapp.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * {@code ApiErrorDTO} is a custom error response class which holds the erros. 
 * @author Umesh Bhujel
 * @since 1.0
 */
@Data
@NoArgsConstructor
@ToString
public class ApiErrorDTO {
	private Integer status;
	private String message;
	private String description;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime time;
}
