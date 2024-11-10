package com.tyl.usercenterbe.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String msg;

    private String description;

    public BaseResponse(int code, String msg, T data,String description) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code, "", data,"");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),errorCode.getMessage(),null,errorCode.getDescription());
    }

    public BaseResponse(int code, String msg,String description) {
        this.code = code;
        this.msg = msg;
        this.data = null;
        this.description = description;
    }
}
