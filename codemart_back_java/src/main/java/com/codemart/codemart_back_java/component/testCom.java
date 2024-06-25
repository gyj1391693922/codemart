package com.codemart.codemart_back_java.component;

import com.codemart.codemart_back_java.exceptions.ServiceException;
import org.springframework.stereotype.Component;

@Component
public class testCom {

    public void a(){
        throw new ServiceException("测试");
    }

}
