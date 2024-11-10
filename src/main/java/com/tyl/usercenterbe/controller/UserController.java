package com.tyl.usercenterbe.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tyl.usercenterbe.common.BaseResponse;
import com.tyl.usercenterbe.common.ErrorCode;
import com.tyl.usercenterbe.common.ResultUtils;
import com.tyl.usercenterbe.exception.BusinessException;
import com.tyl.usercenterbe.model.domain.User;
import com.tyl.usercenterbe.model.domain.request.UserLoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import com.tyl.usercenterbe.model.domain.request.UserRegisterRequest;
import com.tyl.usercenterbe.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tyl.usercenterbe.constant.UserConstant.ADMIN_USER;
import static com.tyl.usercenterbe.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户控制层
 *
 * @author tyl
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private View error;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String code = userRegisterRequest.getCode();
        if(StringUtils.isAllBlank(userAccount,userPassword,checkPassword,code)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long result = userService.userRegister(userAccount, userPassword, checkPassword, code);
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request == null){
            return null;
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> userCurrent(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if(user == null){
            return null;
        }
        long id = user.getId();
        User current = userService.getById(id);
        User safetyUser = userService.getSafetyUser(current);
        return ResultUtils.success(safetyUser);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest,HttpServletRequest request){
        if(userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAllBlank(userAccount,userPassword)){
            return null;
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }
    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(String username , HttpServletRequest request){
        if(isAdmin(request)){
            return ResultUtils.success(new ArrayList<>());
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("userName",username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> result = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(result);
    }
    @PostMapping("/delete")
    public BaseResponse<Boolean> userDelete(@RequestBody long userId ,HttpServletRequest request){
        if(isAdmin(request)){
            return ResultUtils.success(false);
        }
        if(userId <=0){
            return ResultUtils.success(false);
        }
        boolean result = userService.removeById(userId);
        return ResultUtils.success(result);

    }
    private boolean isAdmin(HttpServletRequest request){
        //仅管理员可用
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;//取出user信息
        return user == null || user.getUserRole() != ADMIN_USER;
    }
}
