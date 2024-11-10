package com.tyl.usercenterbe.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 * @author tyl
 */
@Data
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -798329837L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String code;
}
