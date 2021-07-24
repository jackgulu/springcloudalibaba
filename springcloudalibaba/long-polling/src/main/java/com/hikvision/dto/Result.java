package com.hikvision.dto;

import lombok.Data;

@Data
public class Result<T> {
    private T data;
    private Integer code;
    private Boolean success;
}