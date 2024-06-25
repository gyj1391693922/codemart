package com.codemart.codemart_back_java.exceptions;

import com.codemart.codemart_back_java.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class exceptionHandle {


    @ExceptionHandler(value = ServiceException.class)
    public R serviceexceptionError(ServiceException e){
        e.printStackTrace();
        log.error(e.getMessage());
        return R.error(e.getCode(),e.getMessage());
    }

//    @ExceptionHandler(value = Exception.class)
//    public R exceptionError(Exception e){
//        e.printStackTrace();
//        log.error(e.getMessage());
//        return R.error("系统错误");
//    }
//
//    @ExceptionHandler(value = RuntimeException.class)
//    public R runtimeExceptionError(RuntimeException e){
//        log.error(e.getMessage());
//        return R.error("异常");
//    }
}
