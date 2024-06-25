package com.codemart.codemart_back_java.mapper;

import com.codemart.codemart_back_java.entity.MyBiddingInfo;
import com.codemart.codemart_back_java.entity.MyProjects;
import com.codemart.codemart_back_java.entity.UserDetail;
import com.codemart.codemart_back_java.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    @Select("select u_id,nickname from system_user_login where u_id = #{userid}")
    UserInfo userInfo(Integer userid);

    @Select("""
select
            username,
            nickname,
            phone,
            birthday,
            sku,
            address
        from
            (select 
                * 
            from system_user_login 
            where u_id = #{userid}) as t2
        left join
            system_user_info
        using(u_id)
        left join
            (select 
                u_id,
                group_concat(sku_name) sku 
            from system_user_sku 
            left join sku_info 
            using(sku_id) 
            group by u_id) as t1
        using(u_id)
""")
    UserDetail userDetail(Integer userid);

    @Select("""
        select
            de_id,
            money,
            term_day,
            title,
            person,
            create_time,
            operate_time,
            status_name
        from
            (select
                *
            from
                demand_detail
            where author_id = #{uid}) dd
        left join
            demand_info
        using(de_id)
        left join
            demand_status
        using(de_id)
        left join
            demand_status_info
        using(status_id)
""")
    List<MyProjects> getMyProject(Integer uid);

    @Update("update system_user_login set nickname = #{nickname} where u_id = #{uid}")
    void updateUserLogin(Integer uid, String nickname);
    @Update("update system_user_info set address = #{address},birthday = #{birthday},phone = #{phone} where u_id = #{uid}")
    void updateUserInfo(Integer uid,String address,String birthday,String phone);

    @Insert("insert into system_user_sku (u_id,sku_id) values (#{userid},#{sku})")
    void updateUserSku(Integer userid, String sku);


    @Select("""
        select 
            title,
            join_time,
            status_name,
            de_id
        from (select * from demand_user_info 
        where u_id = #{userid}) as dui
        left join
            demand_status
        using(de_id)
        left join
            demand_status_info
        using(status_id)
        left join
            demand_info
        using(de_id)
""")
    List<MyBiddingInfo> listMyBidding(Integer userid);
}
