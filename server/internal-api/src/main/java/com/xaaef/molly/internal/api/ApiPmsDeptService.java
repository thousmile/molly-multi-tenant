package com.xaaef.molly.internal.api;

import com.xaaef.molly.internal.dto.PmsDeptDTO;

import java.util.Map;
import java.util.Set;


/**
 * <p>
 * 部门
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 10:17
 */

public interface ApiPmsDeptService {

    /**
     * 根据 部门ID 获取 部门信息
     */
    Map<Long, PmsDeptDTO> mapByDeptIds(Set<Long> deptIds);


    /**
     * 获取 子部门
     */
    Set<Long> listChildIdByDeptId(Long deptId);


    /**
     * 获取 子部门
     */
    Set<PmsDeptDTO> listChildByDeptId(Long deptId);

}
