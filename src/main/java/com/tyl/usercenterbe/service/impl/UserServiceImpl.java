package com.tyl.usercenterbe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyl.usercenterbe.common.ErrorCode;
import com.tyl.usercenterbe.exception.BusinessException;
import com.tyl.usercenterbe.mapper.UserMapper;
import com.tyl.usercenterbe.model.domain.User;
import com.tyl.usercenterbe.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tyl.usercenterbe.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author tyl
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-10-13 11:01:26
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    /**
     *
     * @param userAccount 用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 返回用户ID
     */

    @Resource
    private UserMapper usermapper;
    private static final String SALT = "tyl";
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String code) {
        //校验
        //使用Apache Commons Lang
        if(StringUtils.isAllBlank(userAccount,userPassword,checkPassword,code)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户长度小于4");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码长度小于8");
        }
        if(code.length()>6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"校验码长度大于6");
        }
        //账户不包含特殊字符
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t";
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(userAccount);
        if(m.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户包含特殊字符");
        }
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不相同");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);
        if(count != 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号重复");
        }
        //账户不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",code);
        count = this.count(queryWrapper);
        if(count != 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"校验码重复");
        }
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setCode(code);
        boolean save = this.save(user);
        if(!save){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"注册失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if(StringUtils.isAllBlank(userAccount,userPassword)){
            return null;
        }
        if(userAccount.length()<=4){
            return null;
        }
        if (userPassword.length() < 8){
            return null;
        }
        //账户不包含特殊字符
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t";
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(userAccount);
        if(m.find()){
            return null;
        }
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = usermapper.selectOne(queryWrapper);
        if(user == null){
            log.info("user login failed ,userAccount cannot match userPassword");
            return null;
        }
        User safetyUser = getSafetyUser(user);
        //记录登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }
    @Override
    public User getSafetyUser(User user){
        if(user == null){
            return null;
        }
        //用户脱敏
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setCode(user.getCode());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setIsDelete(user.getIsDelete());
        return safetyUser;

    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




