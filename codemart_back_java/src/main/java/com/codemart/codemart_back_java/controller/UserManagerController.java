package com.codemart.codemart_back_java.controller;

import com.codemart.codemart_back_java.common.R;
import com.codemart.codemart_back_java.component.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController("/user")
public class UserManagerController {

    @Autowired
    RedisCache redisCache;

//    @PreAuthorize("hasAuthority('user:manager:list')")
    @GetMapping("/list")
    public R userList(){

        return R.success();
    }

}
