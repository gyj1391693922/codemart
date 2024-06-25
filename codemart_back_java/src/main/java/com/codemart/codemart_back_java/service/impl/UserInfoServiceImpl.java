package com.codemart.codemart_back_java.service.impl;

import com.codemart.codemart_back_java.entity.MyBiddingInfo;
import com.codemart.codemart_back_java.entity.MyProjects;
import com.codemart.codemart_back_java.entity.UserDetail;
import com.codemart.codemart_back_java.entity.UserInfo;
import com.codemart.codemart_back_java.exceptions.ServiceException;
import com.codemart.codemart_back_java.mapper.ProjectMapper;
import com.codemart.codemart_back_java.mapper.UserInfoMapper;
import com.codemart.codemart_back_java.service.UserInfoService;
import com.codemart.codemart_back_java.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    ProjectMapper projectMapper;

    @Override
    public UserInfo userInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        if(token != null){
            Integer userid;
            try {
                Claims claims = JwtUtil.parseJWT(token);
                userid = Integer.valueOf(claims.getSubject());
            } catch (Exception e) {
                throw new ServiceException("777","登录信息有误,请重新登录");
            }
            return userInfoMapper.userInfo(userid);
        }else{
            return null;
        }
    }

    @Override
    public UserDetail userDetail(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null) {
            Integer userid;
            try {
                Claims claims = JwtUtil.parseJWT(token);
                userid = Integer.valueOf(claims.getSubject());
            } catch (Exception e) {
                throw new ServiceException("登录信息有误,请重新登录");
            }
            UserDetail userDetail = userInfoMapper.userDetail(userid);
            if(userDetail.getSku() != null){
                List<String> collect = Arrays.stream(userDetail.getSku().split(",")).collect(Collectors.toList());
                userDetail.setSkuList(collect);
            }
            return userDetail;
        }else{
            return null;
        }
    }

    @Override
    public List<MyProjects> getMyProject(HttpServletRequest request) {

        String token = request.getHeader("token");
        List<MyProjects> myProject = null;

        if(token != null){
            Integer userid;
            try {
                Claims claims = JwtUtil.parseJWT(token);
                userid = Integer.valueOf(claims.getSubject());
            } catch (Exception e) {
                throw new ServiceException("登录信息有误,请重新登录");
            }
            if(userid != null){
                myProject = userInfoMapper.getMyProject(userid);
            }
            myProject.forEach(pro->{
                if(pro.getStatus_name().equals("正在进行") || pro.getStatus_name().equals("准备阶段")){
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowDate = df.format(new Date());
                    try {
                        Date cT = df.parse(pro.getCreate_time());
//                        long sup = (df.parse(nowDate).getTime() - cT.getTime()) / 1000;
                        long sup = (df.parse(nowDate).getTime() - cT.getTime()) / 1000 / 60 / 60 / 24;
                        if(sup >= Long.parseLong(pro.getTerm_day())){
                            projectMapper.updateProjectStatus(pro.getDe_id(),6);
                            projectMapper.updateProjectPlan(pro.getDe_id(),6);
                            pro.setStatus_name("已截止");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new ServiceException("异常,请联系管理人员");
                    }
                }
            });
        }
        return myProject;
    }

    @Override
    @Transactional
    public void updateUserDetail(HttpServletRequest request, UserDetail ud) {
        String token = request.getHeader("token");
        if (token != null) {
            Integer userid;
            try {
                Claims claims = JwtUtil.parseJWT(token);
                userid = Integer.valueOf(claims.getSubject());
            } catch (Exception e) {
                throw new ServiceException("登录信息有误,请重新登录");
            }

            if(ud.getBirthday().equals("未设置")){
                ud.setBirthday(null);
            }
            if(ud.getPhone().equals("未设置")){
                ud.setPhone(null);
            }
            if(ud.getAddress().equals("未设置")){
                ud.setAddress(null);
            }

            try{
                userInfoMapper.updateUserLogin(userid,ud.getNickname());
                userInfoMapper.updateUserInfo(userid,ud.getAddress(),ud.getBirthday(),ud.getPhone());
                ud.getSkuList().forEach(sku->{
                    userInfoMapper.updateUserSku(userid,sku);
                });
            }catch (Exception e){
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }

    }

    @Override
    public List<MyBiddingInfo> listMyBidding(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null) {
            Integer userid;
            try {
                Claims claims = JwtUtil.parseJWT(token);
                userid = Integer.valueOf(claims.getSubject());
            } catch (Exception e) {
                throw new ServiceException("登录信息有误,请重新登录");
            }
            try{
                return userInfoMapper.listMyBidding(userid);
            }catch (Exception e){
                throw new ServiceException("异常");
            }
        }


        return null;
    }
}
