package com.codemart.codemart_back_java.service;

import com.codemart.codemart_back_java.common.R;
import com.codemart.codemart_back_java.entity.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ProjectService {
    List<ProjectInfo> getProjectInfo();

    List<ProjectInfo> getProjectInfo(String tag);

    ProjectDetail getProjectDetailByID(HttpServletRequest request, Integer deid);

    List<PlansInfo> getPlans();

    List<SkuInfo> getSkus();

    void saveProject(HttpServletRequest request,ProjectSaveInfo psi);

    void joinProject(joinProjectInfo jpi);

    List<listPerson> listJoinPerson(String token,Integer de_id);

    void closeProject(HttpServletRequest request, Integer de_id);

    void closeJoinProject(HttpServletRequest request, Object cj);

    void isBid(HttpServletRequest request, Object obj);
}
