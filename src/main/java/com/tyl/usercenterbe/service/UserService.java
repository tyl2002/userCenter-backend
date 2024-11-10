package com.tyl.usercenterbe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyl.usercenterbe.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author tyl
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-10-13 11:01:26
*/
public interface UserService extends IService<User> {
    /**
     *
     * @param userAccount 用户名
     * @param userPassword 密码
     * @param checkPassword 校验密码
     * @param code 编号
     * @return long 用户ID
     */
    long userRegister(String userAccount,String userPassword,String checkPassword, String code);

    /**
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return User 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     *
     * @param user
     * @return User 脱敏的用户信息
     */
    User getSafetyUser(User user);

    int userLogout(HttpServletRequest request);
}
