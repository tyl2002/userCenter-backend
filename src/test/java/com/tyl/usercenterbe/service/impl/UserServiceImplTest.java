package com.tyl.usercenterbe.service.impl;

import com.tyl.usercenterbe.model.domain.User;
import com.tyl.usercenterbe.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    private UserService userService;
    @Test
    void testAddUser() {
        User user = new User();
        user.setUserName("tyl");
        user.setUserAccount("123");
        user.setAvatarUrl("https://www.code-nav.cn/static/vip.a1ea732e.svg");
        user.setGender(1);
        user.setUserPassword("123456");
        user.setPhone("123456");
        user.setEmail("12456");
        user.setUserStatus(1);

        boolean a = userService.save(user);
        System.out.println(user.getId());
        assertTrue(a);
    }

    @Test
    void userRegister() {
//        String userAccount = "t】。yl111";
//        String userPassword = "12345678";
//        String checkPassword = "12345678";
//        long result = userService .userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,result);
//        userPassword = "";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,result);
//        userAccount = "tyl";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,result);
//        userAccount = "tyl11";
//        userPassword = "123456";
//        checkPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,result);
//        userAccount = "tyl1111";
//        userPassword = "12345678";
//        checkPassword = "123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,result);
//        userAccount = "tyltyl";
//        userPassword = "123456789";
//        checkPassword = "123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertTrue(result>=0);




    }

    @Test
    void userLogin() {
        String userAccount = "tyl0000";
        String userPassword = "123456789";
        User user = userService.userLogin(userAccount, userPassword, null);
        System.out.println(user);
    }
}