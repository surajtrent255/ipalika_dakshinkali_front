package com.ishanitech.ipalikawebapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YourDataObject {
    private String title;
    private Map<String, Double> rows;
}