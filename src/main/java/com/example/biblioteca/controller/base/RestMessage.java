package com.example.biblioteca.controller.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestMessage {
    private String message;
    private Boolean success = false;
    private LinkedHashMap<String, Object> data = new LinkedHashMap<>();
}