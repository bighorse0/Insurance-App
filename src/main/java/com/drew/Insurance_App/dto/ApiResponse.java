package com.drew.Insurance_App.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
}
