package com.codemart.codemart_back_java.mapper;

import com.codemart.codemart_back_java.entity.ProjectSaveInfo;
import com.codemart.codemart_back_java.entity.UserRegister;
import com.codemart.codemart_back_java.entity.UserResult;
import com.codemart.codemart_back_java.entity.UserRole;
import com.codemart.codemart_back_java.exceptions.ServiceException;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import scala.Int;

import java.util.List;

@Mapper
public interface UserLoginMapper {

    @Select("""
        select 
            u_id,
            username,
            address,
            birthday
        from 
        (
            select * from system_user_login where username = #{username} and password = #{password}
        ) t1
        left join
        system_user_info
        using(u_id)
        """)
    UserResult selectByUsername(String username,String password);

    @Select("""
        select 
            username
        from 
        (
            select * from system_user_login where username = #{username}
        ) t1
        left join
        system_user_info
        using(u_id)
        """)
    String showUserName(String username);

    @Select("""
        select
            per_page
        from
        (
            select * from system_user_login where username = #{username}
        ) t1
        left join
        system_user_role
        using(u_id)
        left join
        system_role_permission
        using(role_id)
        left join
        system_permissions
        using(per_id)
        """)
    List<UserRole> selectByUsernameAndRole(String username);

    @Insert("""
        insert into system_user_login (username,password) values(#{username},#{password});
""")
    @Options(useGeneratedKeys = true, keyProperty = "uId", keyColumn = "u_id")
    int registerUserLogin(UserRegister ur);

    @Insert("insert into system_user_info (u_id) values(#{u_id})")
    void registerUserInfo(Integer u_id);

    @Insert("insert into system_user_role (u_id,role_id) values (#{u_id},1)")
    void registerUserRole(Integer u_id);

    default void register(UserRegister ur, UserLoginMapper mapper) {
        int rowsAffected = mapper.registerUserLogin(ur);
        Integer lastInsertedId = ur.getUId();

        if (rowsAffected > 0 && lastInsertedId != null) {
            try{
                mapper.registerUserInfo(lastInsertedId);
                mapper.registerUserRole(lastInsertedId);
            }catch (Exception e){
                e.printStackTrace();
                throw new ServiceException("注册失败,请联系管理人员");
            }
        } else {
            throw new ServiceException("注册失败");
        }
    }
}
