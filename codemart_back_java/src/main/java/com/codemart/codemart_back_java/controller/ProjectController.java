package com.codemart.codemart_back_java.controller;

import com.codemart.codemart_back_java.common.R;
import com.codemart.codemart_back_java.entity.ProjectInfo;
import com.codemart.codemart_back_java.entity.ProjectSaveInfo;
import com.codemart.codemart_back_java.entity.closeP;
import com.codemart.codemart_back_java.entity.joinProjectInfo;
import com.codemart.codemart_back_java.exceptions.ServiceException;
import com.codemart.codemart_back_java.service.ProjectService;
import com.codemart.codemart_back_java.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("/project/list")
    public R ProjectList(@RequestParam(required = false,defaultValue = "") String tag){
        if(tag == null || tag.equals("") || tag.equals("undefined")){
            List<ProjectInfo> projectInfo = projectService.getProjectInfo();
            return R.success(projectInfo);
        }else{
            List<ProjectInfo> projectInfo = projectService.getProjectInfo(tag);
            return R.success(projectInfo);
        }
    }

    @GetMapping("/project/detail")
    public R ProjectDetail(HttpServletRequest request, @RequestParam Integer id){
        return R.success(projectService.getProjectDetailByID(request, id));
    }

    @GetMapping("/project/plans")
    public R ProjectPlans(){
        return R.success(projectService.getPlans());
    }

    @GetMapping("/project/skus")
    public R getSkuList(){
        return R.success(projectService.getSkus());
    }

    @PostMapping("/project/save")
    public R SaveProject(HttpServletRequest request, @RequestBody  ProjectSaveInfo psi){

        if(psi == null){
            return R.error("发布失败,请检查是否有违规信息,或联系客服解决");
        }else{
            projectService.saveProject(request,psi);
            return R.success("添加成功");
        }
    }

    @PostMapping("/joinProject")
    public R joinProject(@RequestBody joinProjectInfo jpi){
        if(jpi.getT() == null){
            return R.error("请先登陆后再尝试");
        }else if(jpi.getDeId() == null){
            return R.error("错误,没有找到提交的需求");
        }else{
            projectService.joinProject(jpi);
            return R.success("竞标成功,请耐心等待雇主联系");
        }
    }

    @GetMapping("/listJoinPerson")
    public R listJoinPerson(HttpServletRequest request, @RequestParam Integer de_id) {
        String token = request.getHeader("token");
        if (token == null) {
            return R.error("请登录后重试");
        } else {
            return R.success(projectService.listJoinPerson(token,de_id));
        }
    }

    @PostMapping("/closeProject")
    public R closeProject(HttpServletRequest request, @RequestBody closeP obj){
        projectService.closeProject(request, obj.getDe_id());
        return R.success("200","取消发布需求成功");
    }

    @PostMapping("/closeJoin")
    public R closeJoin(HttpServletRequest request,@RequestBody Object cj){
        projectService.closeJoinProject(request,cj);
        return R.success("200","取消竞标成功");
    }

    @PostMapping("/isBid")
    public R isBid(HttpServletRequest request,@RequestBody Object obj){

        projectService.isBid(request,obj);
        return R.success("选标成功,选标后无法修改");
    }
}