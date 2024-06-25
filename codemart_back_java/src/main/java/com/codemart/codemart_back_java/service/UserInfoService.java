package com.codemart.codemart_back_java.service;

import com.codemart.codemart_back_java.entity.MyBiddingInfo;
import com.codemart.codemart_back_java.entity.MyProjects;
import com.codemart.codemart_back_java.entity.UserDetail;
import com.codemart.codemart_back_java.entity.UserInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserInfoService {

    UserInfo userInfo(HttpServletRequest request);

    UserDetail userDetail(HttpServletRequest request);

    List<MyProjects> getMyProject(HttpServletRequest request);

    void updateUserDetail(HttpServletRequest request, UserDetail ud);

    List<MyBiddingInfo> listMyBidding(HttpServletRequest request);
}
