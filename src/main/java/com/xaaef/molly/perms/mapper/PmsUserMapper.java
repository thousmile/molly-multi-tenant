package com.xaaef.molly.perms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.perms.entity.PmsUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;


public interface PmsUserMapper extends BaseMapper<PmsUser> {

    /**
     * 删除某个用户，拥有的角色
     */
    @Delete("delete from pms_user_role where user_id = #{userId}")
    int deleteHaveRoles(Long userId);


    /**
     * 用户关联多个角色
     */
    int insertByRoles(@Param("userId") Long userId,
                      @Param("roles") Set<Long> roles);


    /**
     * 判断手机号是否存在
     */
    @Select("select count(*) from pms_user where mobile = #{mobile}")
    Integer existMobile(String mobile);

    /**
     * 判断邮箱是否存在
     */
    @Select("select count(*) from pms_user where email = #{email}")
    Integer existEmail(@Param("email") String email);

    /**
     * 判断用户名是否存在
     */
    @Select("select count(*) from pms_user where username = #{username}")
    Integer existUsername(@Param("username") String username);


}