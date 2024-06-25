package com.codemart.codemart_back_java.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.codemart.codemart_back_java.common.R;
import com.codemart.codemart_back_java.entity.UserLogin;
import com.codemart.codemart_back_java.entity.UserRegister;
import com.codemart.codemart_back_java.entity.UserResult;
import com.codemart.codemart_back_java.entity.UserRole;
import com.codemart.codemart_back_java.exceptions.ServiceException;
import com.codemart.codemart_back_java.mapper.UserLoginMapper;
import com.codemart.codemart_back_java.service.UserLoginService;
import com.codemart.codemart_back_java.util.JwtUtil;
import com.codemart.codemart_back_java.component.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserLoginLoginServiceImpl implements UserLoginService {

    @Autowired
    UserLoginMapper userLoginMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public R login(UserLogin userLogin) {
        UserResult user = userLoginMapper.selectByUsername(userLogin.getUsername(),userLogin.getPassword());
        if(Objects.isNull(user)){
            throw new ServiceException("405","用户名或密码错误");
        }
//        获取角色
        List<UserRole> userRole = userLoginMapper.selectByUsernameAndRole(userLogin.getUsername());
        String r_json = JSON.toJSONString(userRole);
        String role_json = ("{\"page\":"+r_json+"}");
        //生成token
        String userId = user.getU_id().toString();
        String jwt = JwtUtil.createJWT(userId);
//        authenticate存入redis
        redisCache.setCacheObject("user_id:"+userId,role_json,2, TimeUnit.HOURS);

        HashMap<String,Object> map = new HashMap<>();
        map.put("token",jwt);
        map.put("page",userRole);
        return R.success(map,"登陆成功");
    }

    @Override
    public R register(UserRegister userRegister) {
        String un = userLoginMapper.showUserName(userRegister.getUsername());
        if(!Objects.equals(un, userRegister.getUsername())){
            userLoginMapper.register(userRegister,userLoginMapper);
            return R.success("注册成功");
        }else{
            return R.error("用户名已存在,请尝试修改");
        }
    }
}
