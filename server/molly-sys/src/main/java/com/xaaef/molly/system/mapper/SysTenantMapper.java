package com.xaaef.molly.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.system.entity.SysTenant;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.scheduling.annotation.Async;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


public interface SysTenantMapper extends BaseMapper<SysTenant> {

    // 查询 数据库 所有的 表名称
    @Select("SELECT DISTINCT TABLE_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = (SELECT DATABASE ())")
    Set<String> selectListTableNames();


    // 清空指定表格的数据
    int truncateTableData(@Param("tableNames") Set<String> tableNames);


    // 删除 租户 的所有权限模板
    @Delete("delete from sys_tenant_template WHERE tenant_id = #{tenantId}")
    int deleteHaveTemplates(String tenantId);


    // 租户新增模板权限
    int insertByTemplates(@Param("tenantId") String tenantId,
                          @Param("templates") Set<Long> templates);


}