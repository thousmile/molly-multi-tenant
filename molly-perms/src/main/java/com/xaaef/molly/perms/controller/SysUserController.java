package com.xaaef.molly.perms.controller;

import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.perms.entity.SysUser;
import com.xaaef.molly.perms.service.SysUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 首页 控制器
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 3.0
 */


@Slf4j
@RestController
@RequestMapping("/pms/user")
@AllArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @GetMapping
    public JsonResult<List<SysUser>> list() {
        return JsonResult.success(
                userService.findAll()
        );
    }

}
