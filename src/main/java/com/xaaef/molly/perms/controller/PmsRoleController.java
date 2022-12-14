package com.xaaef.molly.perms.controller;

import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.service.PmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 3.0
 */


@Slf4j
@RestController
@RequestMapping("/pms/role")
@Api(tags = "[ 权限 ] 角色")
@AllArgsConstructor
public class PmsRoleController {

    private final PmsRoleService roleService;


    @ApiOperation(value = "", notes = "客户端认证模式")
    @GetMapping
    public JsonResult<List<PmsRole>> list() {
        return JsonResult.success(roleService.list());
    }


}
