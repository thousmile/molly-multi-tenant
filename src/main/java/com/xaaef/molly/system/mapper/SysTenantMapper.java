package com.xaaef.molly.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.system.entity.SysTenant;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.Set;


public interface SysTenantMapper extends BaseMapper<SysTenant> {


    // 删除 租户 的所有权限模板
    @Delete("delete from sys_tenant_template WHERE tenant_id = #{tenantId}")
    int deleteHaveTemplates(String tenantId);


    // 租户新增模板权限
    int insertByTemplates(@Param("tenantId") String tenantId,
                          @Param("templates") Set<Long> templates);


}