package com.xaaef.molly.internal.api;

import com.xaaef.molly.internal.dto.CmsProjectDTO;

import java.util.Set;


/**
 * <p>
 * 项目
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/14 10:17
 */

public interface ApiCmsProjectService {

    /**
     * 根据 项目ID 获取简单的项目信息。如：id，名称，
     *
     * @author WangChenChen
     * @date 2023/8/11 10:47
     */
    CmsProjectDTO getSimpleById(Long projectId);

}
