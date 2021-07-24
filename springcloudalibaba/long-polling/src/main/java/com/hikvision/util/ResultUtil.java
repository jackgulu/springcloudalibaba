package com.hikvision.util;

import com.hikvision.dto.Result;

public class ResultUtil {
    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        result.setSuccess(true);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(200);
        result.setData(data);
        return result;
    }
}
