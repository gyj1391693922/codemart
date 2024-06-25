package com.codemart.codemart_back_java.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codemart.codemart_back_java.common.R;
import com.codemart.codemart_back_java.entity.*;
import com.codemart.codemart_back_java.exceptions.ServiceException;
import com.codemart.codemart_back_java.mapper.ProjectMapper;
import com.codemart.codemart_back_java.service.ProjectService;
import com.codemart.codemart_back_java.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
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
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectMapper projectMapper;

    @Override
    @Transactional
    public List<ProjectInfo> getProjectInfo() {
        List<ProjectInfo> projectInfo = projectMapper.getProjectInfo();
        if(projectInfo == null){
            return projectInfo;
        }
        projectInfo.forEach(pi->{
            if(pi.getContext() == null){
                pi.setContext("该项目没有描述");
            }
            pi.setSkuList(Arrays.stream(pi.getSku().split(",")).collect(Collectors.toList()));

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowDate = df.format(new Date());
                try {
                    Date cT = df.parse(pi.getCreate_time());
//                        long sup = (df.parse(nowDate).getTime() - cT.getTime()) / 1000;
                    long sup = (df.parse(nowDate).getTime() - cT.getTime()) / 1000 / 60 / 60 / 24;
                    if(sup >= pi.getTerm_day()){
                        try{
                            projectMapper.updateProjectStatus(pi.getDe_id(),6);
                            projectMapper.updateProjectPlan(pi.getDe_id(),6);
                        }catch (Exception e){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }

                    }
                } catch (ParseException e) {
                    throw new ServiceException("异常,请联系管理人员");
                }
        });
        return projectInfo;
    }

    @Override
    public List<ProjectInfo> getProjectInfo(String tag) {
        List<ProjectInfo> projectInfo = projectMapper.getProjectInfoByTag(tag);
        if(projectInfo == null){
            return projectInfo;
        }
        projectInfo.forEach(pi->{
            if(pi.getContext() == null){
                pi.setContext("该项目没有描述");
            }
            pi.setSkuList(Arrays.stream(pi.getSku().split(",")).collect(Collectors.toList()));
        });
        return projectInfo;
    }

    @Override
    @Transactional
    public ProjectDetail getProjectDetailByID(HttpServletRequest request, Integer deid) {

        String token = request.getHeader("token");
        Integer userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new ServiceException("登录信息有误,请重新登录");
        }

        ProjectDetail projectDetail = projectMapper.getProjectDetail(deid);

        if(projectDetail.getStatus().equals("正在进行") || projectDetail.getStatus().equals("准备阶段")){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = df.format(new Date());
            try {
                Date cT = df.parse(projectDetail.getCreate_time());
                long sup = (df.parse(nowDate).getTime() - cT.getTime()) / 1000;
                if(sup < 0){
                    try{
                        projectMapper.updateProjectStatus(projectDetail.getId(),6);
                        projectMapper.updateProjectPlan(projectDetail.getId(),6);
                        projectDetail.setStatus("已截止");
                        projectDetail.setPlan_id(5);
                    }catch (Exception e){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            } catch (ParseException e) {
                throw new ServiceException("异常,请联系管理人员");
            }
        }

        if(userid.equals(projectDetail.getAuthor_id())){
            projectDetail.setPageStatus(true);
        }else{
            projectDetail.setPageStatus(false);
        }


        return projectDetail;
    }

    @Override
    public List<PlansInfo> getPlans() {
        return projectMapper.getPlans();
    }

    @Override
    public List<SkuInfo> getSkus() {
        return projectMapper.getSkus();
    }

    @Override
    @Transactional
    public void saveProject(HttpServletRequest request,
                            ProjectSaveInfo psi) {
        String token = request.getHeader("token");
        Integer userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new ServiceException("登录信息有误,请重新登录");
        }
        psi.setAuthorId(userid);
        if(StringUtil.isNullOrEmpty(psi.getTitle()) || psi.getMoney() == 0){
            throw new ServiceException("注意,标题不能为空、金额不能为0");
        }else{
            try{
                projectMapper.projectSave(psi,projectMapper);
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }

    @Override
    @Transactional
    public void joinProject(joinProjectInfo jpi) {
        String token = jpi.getT();
        Integer userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new ServiceException("登录信息有误,请重新登录");
        }
        PList jp = projectMapper.showJoinProjectById(userid, jpi.getDeId());
        if(jp != null && jpi.getDeId().equals(jp.getDe_id()) && !jp.getStatus_id().equals(2)){
            throw new ServiceException("409","您已经加入过该需求,不能再次竞标");
        }else{
            try{
                if(jp != null && jp.getStatus_id().equals(2)){
                    projectMapper.closeJoinProject(jp.getDui_id(),1);
                }else{
                    jpi.setUser_id(userid);
                    projectMapper.joinProject(jpi);
                    projectMapper.updatePerson(jpi.getDeId());
                    projectMapper.setDemandUserInfo(jpi.getDui_id());
                }
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                e.printStackTrace();
                throw new ServiceException("错误");
            }
        }
    }

//    @Override
//    @Transactional
//    public void setBidding(Integer userid,joinProjectInfo jpi){
//        try{
//            Integer duiId = projectMapper.selectJoinProject(userid, jpi.getDeId());
//            projectMapper.setDemandUserInfo(duiId);
//        }catch (Exception e) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            e.printStackTrace();
//            throw new ServiceException("错误");
//        }
//    }

    @Override
    public List<listPerson> listJoinPerson(String token,Integer de_id) {
        Integer userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new ServiceException("登录信息有误,请重新登录");
        }

        List<listPerson> listPeople = projectMapper.listJoinPerson(de_id);

        listPeople.forEach(per->{
            if(per.getSkus() != null){
                List<String> perSkus = Arrays.stream(per.getSkus().split(",")).collect(Collectors.toList());
                per.setSkusList(perSkus);
            }
            if(userid.equals(per.getU_id())){
                per.setShow("true");
            }else{
                per.setShow("false");
            }
        });
        return listPeople;
    }

    @Override
    public void closeProject(HttpServletRequest request, Integer de_id) {
        String token = request.getHeader("token");
        Integer userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new ServiceException("登录信息有误,请重新登录");
        }

        Integer uid = projectMapper.showDemandIsWho(de_id);
        if(uid.equals(userid)){
            projectMapper.updateProjectStatus(de_id,2);
            projectMapper.updateProjectPlan(de_id,6);
        }
    }

    @Override
    @Transactional
    public void closeJoinProject(HttpServletRequest request, Object cj) {
        String token = request.getHeader("token");
        Integer userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new ServiceException("登录信息有误,请重新登录");
        }

        JSONObject json = (JSONObject) JSONObject.toJSON(cj);
        JSONObject j1 = json.getJSONObject("cj");
        Integer uid = j1.getInteger("uid");
        Integer dui = j1.getInteger("dui");
        if(userid.equals(uid)){
            try{
                projectMapper.closeJoinProject(dui,2);
            }catch (Exception e){
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new ServiceException("失败,请稍后重试或联系管理人员");
            }
        }

    }

    @Override
    @Transactional
    public void isBid(HttpServletRequest request, Object obj) {
        try {
            String token = request.getHeader("token");
            Claims claims = JwtUtil.parseJWT(token);
            Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new ServiceException("登录信息有误,请重新登录");
        }

        try{
            JSONObject json = (JSONObject) JSONObject.toJSON(obj);
            JSONObject j_ob = json.getJSONObject("obj");
            Integer uid = j_ob.getInteger("u_id");
            Integer deid = j_ob.getInteger("de_id");
            projectMapper.isBid(uid,deid);
            projectMapper.updateProjectPlan(deid,3);
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new ServiceException("错误");
        }
    }
}
