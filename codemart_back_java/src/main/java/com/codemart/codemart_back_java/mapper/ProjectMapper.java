package com.codemart.codemart_back_java.mapper;

import com.codemart.codemart_back_java.entity.*;
import com.codemart.codemart_back_java.exceptions.ServiceException;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import scala.Int;

import java.util.List;

@Mapper
public interface ProjectMapper {
    @Select("""
        SELECT
            de_id,
            title,
            sku,
            person,
            de_context AS context,
            create_time,
            term_day
        FROM
        (
            SELECT
                ds.de_id,
                GROUP_CONCAT(si.sku_name) AS sku
            FROM
                demand_sku ds
            LEFT JOIN
                sku_info si
            USING(sku_id)
            GROUP BY
                ds.de_id
        ) t1
        LEFT JOIN
            demand_info di 
        USING(de_id)
        LEFT JOIN
            demand_contenxt dc
        USING(de_id)
        LEFT JOIN
            demand_status
        USING(de_id)
        LEFT JOIN
                demand_detail
        USING(de_id)
        where status_id != 2 and status_id != 6
""")
    List<ProjectInfo> getProjectInfo();

    @Select("""
        SELECT
            de_id,
            title,
            sku,
            person,
            de_context AS context
        FROM
        (
            SELECT
                ds.de_id,
                GROUP_CONCAT(si.sku_name) AS sku
            FROM
                demand_sku ds
            LEFT JOIN
                sku_info si
            USING(sku_id)
            GROUP BY
                ds.de_id
        ) t1
        LEFT JOIN
            demand_info di 
        USING(de_id)
        LEFT JOIN
            demand_contenxt dc
        USING(de_id)
        LEFT JOIN
            demand_status
        USING(de_id)
        LEFT JOIN
                demand_detail
        USING(de_id)
        WHERE status_id != 2 and status_id != 6 and 
        (sku LIKE CONCAT('%', #{tag}, '%') OR title LIKE CONCAT('%', #{tag}, '%'));
""")
    List<ProjectInfo> getProjectInfoByTag(String tag);

    @Select("""
                SELECT
                    de_id id,
                    title,
                    sku,
                    money,
                    author_id,
                    nickname,
                    term_day,
                    de_context context,
                    status_name status,
                    plan_id,
                    create_time
                 FROM
                 (
                     SELECT
                         ds.de_id,
                         GROUP_CONCAT(si.sku_name) AS sku
                     FROM
                         demand_sku ds
         
                     LEFT JOIN
                         sku_info si
                     USING(sku_id)
                     WHERE de_id = #{deid}
                     GROUP BY
                         ds.de_id
                 ) t1
                 LEFT JOIN
                     demand_info di
                 USING(de_id)
                 LEFT JOIN
                     demand_contenxt dc
                 USING(de_id)
                 LEFT JOIN
                     demand_status
                 USING(de_id)
                 LEFT JOIN
                     demand_status_info
                 USING(status_id)
                LEFT JOIN
                        demand_detail
                USING(de_id)
                LEFT JOIN 
                        system_user_login
                ON demand_detail.author_id = system_user_login.u_id
                LEFT JOIN
                        demand_plan
                USING(de_id);
""")
    ProjectDetail getProjectDetail(Integer deid);

    @Select("""
    select
        plan_id,
        plan_name
    from
        plan_info
""")
    List<PlansInfo> getPlans();

    @Select("""
    select
        *
    from
        sku_info
""")
    List<SkuInfo> getSkus();


    @Insert("insert into demand_info (`title`) values (#{title})")
    @Options(useGeneratedKeys = true, keyProperty = "deId", keyColumn = "de_id")
    int insertDemandInfo(ProjectSaveInfo psi);

    @Insert("insert into demand_contenxt (de_id, de_context) values (#{deId}, #{context})")
    void insertDemandContext(@Param("deId") Integer deId, @Param("context") String context);

    default void projectSave(ProjectSaveInfo psi, ProjectMapper mapper) {
        int rowsAffected = mapper.insertDemandInfo(psi);
        Integer lastInsertedId = psi.getDeId();

        if (rowsAffected > 0 && lastInsertedId != null) {
           try{
               mapper.insertDemandContext(lastInsertedId, psi.getContext());
               if(!psi.getSku().isEmpty()){
                   psi.getSku().forEach((sku)->{
                       mapper.insertDemandSku(lastInsertedId,sku);
                   });
               }
               mapper.insertDemandPlan(lastInsertedId);
               mapper.insertDemandDetail(lastInsertedId,psi.getAuthorId(),psi.getMoney(),psi.getDays());
               mapper.insertDemandStatus(lastInsertedId);
           }catch (Exception e){
               e.printStackTrace();
               throw new ServiceException("提交错误,请联系客服或管理人员");
           }
        } else {
            throw new ServiceException("提交错误");
        }
    }

    @Insert("insert into demand_status (de_id,status_id) values (#{deId},1)")
    void insertDemandStatus(Integer deId);

    @Insert("insert into demand_detail (de_id,author_id,money,term_day) values (#{deId}, #{authorId}, #{money}, #{days})")
    void insertDemandDetail(Integer deId, Integer authorId, Double money, Integer days);

    @Insert("insert into demand_plan (de_id,plan_id) values (#{deId}, 1)")
    void insertDemandPlan(Integer deId);

    @Insert("insert into demand_sku (de_id,sku_id) values (#{deId}, #{sku})")
    void insertDemandSku(Integer deId, Integer sku);


    @Update("update demand_status set status_id = #{statusId} where de_id = #{de_id}")
    void updateProjectStatus(Integer de_id, Integer statusId);

    @Select("select dui_id from demand_user_info where de_id = #{deid}")
    List<Integer> getJoinPersonList(Integer deid);

    @Update("update demand_plan set plan_id = #{planId} where de_id = #{de_id}")
    void updateProjectPlan(Integer de_id, Integer planId);

    @Insert("insert into demand_user_info (u_id,de_id) values (#{user_id},#{deId});")
    @Options(useGeneratedKeys = true, keyProperty = "dui_id", keyColumn = "dui_id")
    void joinProject(joinProjectInfo jpi);

    @Select("""
        select 
            de_id,
            dui_id,
            status_id
        from (select * from demand_user_info 
        where u_id = #{userid} 
        and de_id = #{deid}) as dui
        left join demand_user_status
        using(dui_id)
""")
    PList showJoinProjectById(Integer userid, Integer deid);

    @Select("select author_id from demand_detail where de_id = #{deid}")
    Integer showDemandIsWho(Integer deid);

    @Update("update demand_info set person = (select count(1) from demand_user_info where de_id = #{deId}) where de_id = #{deId}")
    void updatePerson(Integer deId);

    @Select("""
        SELECT
                    dui_id,
                	du.u_id,
                	username,
                	nickname,
                	join_time,
                	skus,
        			demand_select_of_dibs.u_id as dsod_dui_id
                from
                	(select * from demand_user_info where de_id = #{de_id}) as du
                left join
                	system_user_info
                using(u_id)
                left join
                	(select u_id,group_concat(sku_name) as skus from system_user_sku left join sku_info using(sku_id) group by u_id) s
                using(u_id)
                left join
                	system_user_login
                using(u_id)
                left join
                    demand_user_status
                using(dui_id)
                left join
                    demand_select_of_dibs
                using(de_id)
                where status_id != 2
""")
    List<listPerson> listJoinPerson(Integer de_id);

    @Insert("insert into demand_user_status (dui_id) values (#{dui_id})")
    void setDemandUserInfo(@Param("dui_id") Integer dui_id);

    @Update("update demand_user_status set status_id = #{statusId} where dui_id = #{dui}")
    void closeJoinProject(Integer dui,Integer statusId);

    @Insert("insert into demand_select_of_dibs (u_id,de_id) values (#{uid},#{deid})")
    void isBid(Integer uid, Integer deid);
}
