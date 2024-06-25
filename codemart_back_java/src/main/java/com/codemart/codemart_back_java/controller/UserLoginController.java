package com.codemart.codemart_back_java.controller;

import com.codemart.codemart_back_java.common.R;
import com.codemart.codemart_back_java.entity.UserLogin;
import com.codemart.codemart_back_java.entity.UserRegister;
import com.codemart.codemart_back_java.service.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
public class UserLoginController {

    @Autowired
    UserLoginService userLoginService;

    @PostMapping("/login")
    public R login(@RequestBody UserLogin userLogin){
        if(!userLogin.getUsername().equals(null) && !userLogin.getPassword().equals(null)){
            return userLoginService.login(userLogin);
        }else{
            return R.error("账号或密码错误,请检查后重新输入");
        }
    }

    @PostMapping("/register")
    public R register(@RequestBody UserRegister userRegister){
        if(!userRegister.getUsername().equals(null) && !userRegister.getPassword().equals(null)){
            return userLoginService.register(userRegister);
        }else{
            return R.error("注册失败,请重试");
        }
    }

    @GetMapping("/test")
    public R test(){
        return R.success("测试成功");
    }

}
