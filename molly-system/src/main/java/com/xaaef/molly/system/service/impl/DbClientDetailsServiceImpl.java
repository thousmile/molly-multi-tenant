package com.xaaef.molly.system.service.impl;

import com.xaaef.molly.core.base.service.BaseService;
import com.xaaef.molly.core.base.service.impl.BaseServiceImpl;
import com.xaaef.molly.system.entity.DbClientDetails;
import com.xaaef.molly.system.repository.DbClientDetailsRepository;
import com.xaaef.molly.system.service.DbClientDetailsService;
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
public class DbClientDetailsServiceImpl extends BaseServiceImpl<DbClientDetailsRepository, DbClientDetails, String>
        implements DbClientDetailsService {


}
