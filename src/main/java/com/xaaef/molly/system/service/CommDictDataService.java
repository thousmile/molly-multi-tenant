package com.xaaef.molly.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.core.tenant.base.service.BaseService;
import com.xaaef.molly.system.entity.CommDictData;
import com.xaaef.molly.system.po.DictQueryPO;
import com.xaaef.molly.system.vo.DictDataVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:33
 */

public interface CommDictDataService extends BaseService<CommDictData> {


    IPage<CommDictData> pageKeywords(DictQueryPO params);


    /**
     * 根据关键字查询
     *
     * @author Wang Chen Chen
     * @date 2021/8/24 18:24
     */
    List<CommDictData> listByKey(String dictTypeKey);


    /**
     * 根据关键字查询
     *
     * @author Wang Chen Chen
     * @date 2021/8/24 18:24
     */
    Map<String, List<DictDataVO>> mapByKeys();

}
