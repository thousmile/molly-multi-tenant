package com.xaaef.molly.system.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface SysUserMapper {


    // 查询 系统用户 关联的租户
    @Select("select tenant_id from sys_user_tenant WHERE user_id = #{userId}")
    Set<String> selectHaveTenants(Long userId);


    // 系统用户 删除关联的租户
    @Delete("delete from sys_user_tenant WHERE user_id = #{userId}")
    int deleteHaveTenants(Long userId);


    // 租户 删除关联的 系统用户
    @Delete("delete from sys_user_tenant WHERE tenant_id = #{tenantId}")
    int deleteHaveSysUser(String tenantId);


    // 系统用户 关联租户
    int insertByTenants(@Param("userId") Long userId,
                        @Param("tenantIds") Set<String> tenantIds);


}
