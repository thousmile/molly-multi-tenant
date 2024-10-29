package com.xaaef.molly.perms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.tenant.ds.DataScope;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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
     * 根据用户id，查询
     */
    List<PmsUser> selectSimpleListByUserIds(
            @Param("dbNameList") Set<String> dbNameList,
            @Param("userIdList") Set<Long> userIdList);



    /**
     * 根据条件列表查询数据
     */
    @DataScope()
    List<PmsUser> selectUserList(@Param("p") PmsUser user);


    /**
     * 根据条件分页查询数据
     */
    @DataScope()
    IPage<PmsUser> selectUserPage(IPage<?> page, @Param("p") PmsUser user);


}