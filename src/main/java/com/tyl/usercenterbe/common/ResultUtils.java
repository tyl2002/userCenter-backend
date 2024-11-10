package com.tyl.usercenterbe.common;

public class ResultUtils {
    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,"ok",data,"");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse<ErrorCode> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse<ErrorCode> error(ErrorCode errorCode,String message,String description){
        return new BaseResponse(errorCode.getCode(),message,description);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse<ErrorCode> error(ErrorCode errorCode,String description){
        return new BaseResponse(errorCode.getCode(),errorCode.getMessage(),description);
    }

    public static BaseResponse<ErrorCode> error(int code,String message,String description){
        return new BaseResponse(code,message,description);
    }

}
