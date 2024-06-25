package com.codemart.codemart_back_java.mapper;

import com.codemart.codemart_back_java.entity.FontCnt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
public interface FontMapper {


    @Select("select count(u_id) cnt from system_user_login")
    FontCnt getUserCnt();

    @Select("select count(de_id) cnt from demand_info")
    FontCnt getProjectCnt();

    @Select("select sum(money) cnt from demand_detail")
    FontCnt getMoneyCnt();
}
