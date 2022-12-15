package com.xaaef.molly.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.system.entity.ChinaArea;

public interface ChinaAreaService extends IService<ChinaArea> {


    IPage<ChinaArea> pageKeywords(SearchPO params);


}
