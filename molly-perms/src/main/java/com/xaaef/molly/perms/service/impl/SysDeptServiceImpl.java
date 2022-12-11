package com.xaaef.molly.perms.service.impl;

import com.xaaef.molly.core.tenant.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.perms.entity.SysDept;
import com.xaaef.molly.perms.repository.SysDeptRepository;
import com.xaaef.molly.perms.service.SysDeptService;
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
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptRepository, SysDept, Long>
        implements SysDeptService {


}
