package com.codemart.codemart_back_java.controller;

import com.codemart.codemart_back_java.common.R;
import com.codemart.codemart_back_java.entity.UserDetail;
import com.codemart.codemart_back_java.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/userInfo")
    public R userInfo(HttpServletRequest request){

        return R.success(userInfoService.userInfo(request));

    }

    @GetMapping("/userDetail")
    public R userDetail(HttpServletRequest request){

        return R.success(userInfoService.userDetail(request));
    }

    @GetMapping("/user/getMyProject")
    public R getMyProject(HttpServletRequest request){

        return R.success(userInfoService.getMyProject(request));
    }

    @PostMapping("/updateUserDetail")
    public R updateUserDetail(HttpServletRequest request, @RequestBody  UserDetail ud){
        userInfoService.updateUserDetail(request,ud);
        return R.success();
    }

    @PostMapping("/listMyBidding")
    public R listMyBidding(HttpServletRequest request){
        return R.success(userInfoService.listMyBidding(request));
    }

}
