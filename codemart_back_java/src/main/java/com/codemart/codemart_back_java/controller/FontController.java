package com.codemart.codemart_back_java.controller;

import com.codemart.codemart_back_java.common.R;
import com.codemart.codemart_back_java.service.impl.FontServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class FontController {

    @Autowired
    FontServiceImpl fontService;

    @GetMapping("/font/userCnt")
    public R getUserCnt(){

        return R.success(fontService.getUserCnt());
    }

    @GetMapping("/font/projectCnt")
    public R getProjectCnt(){
        return R.success(fontService.getProjectCnt());
    }

    @GetMapping("/font/moneyCnt")
    public R getMoneyCnt(){
        return  R.success(fontService.getMoneyCnt());
    }

}
