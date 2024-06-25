package com.codemart.codemart_back_java.controller;

import com.codemart.codemart_back_java.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class ErrorController {

    @RequestMapping("/error/exthrow")
    public void rethrow(HttpServletRequest request) {
        ServiceException attribute = (ServiceException) request.getAttribute("filter.error");
        throw new ServiceException(attribute.getCode(),attribute.getMessage());
    }

}
