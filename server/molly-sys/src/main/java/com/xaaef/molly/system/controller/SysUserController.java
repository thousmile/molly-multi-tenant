package com.xaaef.molly.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.service.SysTenantService;
import com.xaaef.molly.system.service.SysUserService;
import com.xaaef.molly.system.vo.UpdateUserTenantVO;
import com.xaaef.molly.system.vo.UserListTenantVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/sys/user")
@Tag(name = "[ 系统 ] 用户管理")
public class SysUserController {

    public final SysUserService baseService;


    @Operation(summary = "查询关联租户", description = "查询系统用户关联的租户")
    @GetMapping("/list/tenant")
    public JsonResult<UserListTenantVO> listTenant(@RequestParam Long userId) {
        try {
            return JsonResult.success(
                    baseService.listHaveTenants(userId)
            );
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage(), null);
        }
    }


    @Operation(summary = "修改关联租户", description = "修改系统用户关联的租户")
    @PostMapping("/update/tenant")
    public JsonResult<Boolean> updateTemplateId(@RequestBody @Validated UpdateUserTenantVO params,
                                                BindingResult br) {
        try {
            return JsonResult.success(
                    baseService.updateTenant(params.getUserId(), params.getTenantIds())
            );
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage(), Boolean.FALSE);
        }
    }


}