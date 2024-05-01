package com.xaaef.molly.perms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.auth.jwt.JwtLoginUser;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.auth.service.JwtTokenService;
import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidUpdate;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.po.UserQueryPO;
import com.xaaef.molly.perms.service.PmsUserService;
import com.xaaef.molly.perms.vo.ResetPasswordVO;
import com.xaaef.molly.perms.vo.UpdatePasswordVO;
import com.xaaef.molly.perms.vo.UpdateUserRoleIdVO;
import com.xaaef.molly.perms.vo.UserRightsVO;
import com.xaaef.molly.web.repeat.NoRepeatSubmit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.xaaef.molly.tenant.util.DelegateUtils.delegate;


/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 3.0
 */


@Slf4j
@RestController
@RequestMapping("/pms/user")
@Tag(name = "[ 权限 ] 用户")
@AllArgsConstructor
public class PmsUserController {

    private final PmsUserService baseService;

    private final JwtTokenService jwtTokenService;


    @Operation(summary = "用户权限", description = "用户权限")
    @GetMapping("/rights")
    public JsonResult<UserRightsVO> getUserRights() {
        var result = baseService.getUserRights();
        return JsonResult.success(result);
    }


    @Operation(summary = "分页查询", description = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<PmsUser>> pageQuery(UserQueryPO params) {
        IPage<PmsUser> page = baseService.pageKeywords(params);
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @Operation(summary = "在线用户查询", description = "在线用户 查询所有")
    @GetMapping("/online/query")
    public JsonResult<Collection<JwtLoginUser>> onlineQuery() {
        return JsonResult.success(jwtTokenService.mapLoginUser().values());
    }


    @NoRepeatSubmit
    @Operation(summary = "新增", description = "不需要添加id")
    @PostMapping()
    public JsonResult<PmsUser> save(@RequestBody @Validated({ValidCreate.class}) PmsUser entity) {
        baseService.save(entity);
        return JsonResult.success(entity);
    }


    @NoRepeatSubmit
    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping()
    public JsonResult<Boolean> updateById(@RequestBody @Validated({ValidUpdate.class}) PmsUser entity) {
        var flag = baseService.updateById(entity);
        return JsonResult.success(flag);
    }


    @NoRepeatSubmit
    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping("/info")
    public JsonResult<Boolean> updateInfo(@RequestBody @Validated({ValidUpdate.class}) PmsUser entity) {
        // 使用 当前登录的用户 所在的租户ID。修改
        var flag = delegate(JwtSecurityUtils.getTenantId(),
                () -> baseService.updateById(entity));
        return JsonResult.success(flag);
    }


    @NoRepeatSubmit
    @Operation(summary = "删除", description = "修改必须要id")
    @DeleteMapping("/{id}")
    public JsonResult<Boolean> removeById(@PathVariable Long id) {
        var flag = baseService.removeById(id);
        return JsonResult.success(flag);
    }


    @NoRepeatSubmit
    @Operation(summary = "修改用户密码", description = "修改用户密码")
    @PostMapping("/update/password")
    public JsonResult<Boolean> updatePassword(@RequestBody @Validated UpdatePasswordVO data) {
        return JsonResult.success(
                baseService.updatePassword(data)
        );
    }


    @NoRepeatSubmit
    @Operation(summary = "重置用户密码", description = "重置用户密码")
    @PostMapping("/reset/password")
    public JsonResult<Boolean> resetPassword(@RequestBody @Validated ResetPasswordVO data) {
        return JsonResult.success(
                baseService.resetPassword(data)
        );
    }


    @NoRepeatSubmit
    @Operation(summary = "修改用户角色", description = "修改用户角色,会删除之前的角色信息。")
    @PostMapping("/update/roles")
    public JsonResult<Boolean> updateUserRoles(@RequestBody @Validated UpdateUserRoleIdVO data) {
        return JsonResult.success(
                baseService.updateUserRoles(data.getUserId(), data.getRoles())
        );
    }


}
