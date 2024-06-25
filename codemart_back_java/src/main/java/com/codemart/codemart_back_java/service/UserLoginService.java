package com.codemart.codemart_back_java.service;

import com.codemart.codemart_back_java.common.R;
import com.codemart.codemart_back_java.entity.UserLogin;
import com.codemart.codemart_back_java.entity.UserRegister;

public interface UserLoginService {
    R login(UserLogin userLogin);

    R register(UserRegister userRegister);
}
