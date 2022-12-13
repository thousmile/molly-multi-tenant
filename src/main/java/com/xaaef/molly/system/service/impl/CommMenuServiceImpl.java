package com.xaaef.molly.system.service.impl;

import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.system.entity.CommMenu;
import com.xaaef.molly.system.repository.CommMenuRepository;
import com.xaaef.molly.system.service.CommMenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/9 15:36
 */


@Slf4j
@Service
@AllArgsConstructor
public class CommMenuServiceImpl extends BaseServiceImpl<CommMenuRepository, CommMenu, Long>
        implements CommMenuService {



}
