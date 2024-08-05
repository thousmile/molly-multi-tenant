package com.xaaef.molly.perms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaaef.molly.perms.entity.PmsUser;
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

}