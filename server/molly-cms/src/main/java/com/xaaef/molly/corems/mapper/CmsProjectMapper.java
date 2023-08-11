package com.xaaef.molly.corems.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.perms.entity.CmsProject;
import org.apache.ibatis.annotations.Select;

import java.util.Set;


/**
 * <p>
 * 项目
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/14 10:03
 */


public interface CmsProjectMapper extends BaseMapper<CmsProject> {


    // 查询 所有的 不包含 project_id 的表
    @Select("SELECT DISTINCT TABLE_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = (SELECT DATABASE ()) AND COLUMN_NAME != #{column}")
    Set<String> selectNotExistColumnTableNames(String column);


}
