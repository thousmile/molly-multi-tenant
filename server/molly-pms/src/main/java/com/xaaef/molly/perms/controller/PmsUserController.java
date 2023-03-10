package com.xaaef.molly.perms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.po.UserQueryPO;
import com.xaaef.molly.perms.service.PmsUserService;
import com.xaaef.molly.perms.vo.ResetPasswordVO;
import com.xaaef.molly.perms.vo.UpdatePasswordVO;
import com.xaaef.molly.perms.vo.UpdateUserRoleIdVO;
import com.xaaef.molly.perms.vo.UserRightsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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


    @Operation(summary = "新增", description = "不需要添加id")
    @PostMapping()
    public JsonResult<PmsUser> create(@RequestBody PmsUser entity) {
        try {
            baseService.save(entity);
            return JsonResult.success(entity);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage(), PmsUser.class);
        }
    }



    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping()
    public JsonResult update(@RequestBody PmsUser entity) {
        try {
            boolean byId = baseService.updateById(entity);
            return JsonResult.success(byId);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @Operation(summary = "删除", description = "修改必须要id")
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable Long id) {
        try {
            boolean b = baseService.removeById(id);
            return JsonResult.success(b);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }



    @Operation(summary = "修改用户密码", description = "修改用户密码")
    @PostMapping("/update/password")
    public JsonResult updatePassword(@RequestBody @Validated UpdatePasswordVO data,
                                     BindingResult br) {
        try {
            return JsonResult.success(
                    baseService.updatePassword(data)
            );
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @Operation(summary = "重置用户密码", description = "重置用户密码")
    @PostMapping("/reset/password")
    public JsonResult resetPassword(@RequestBody @Validated ResetPasswordVO data,
                                    BindingResult br) {
        try {
            return JsonResult.success(
                    baseService.resetPassword(data)
            );
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @Operation(summary = "修改用户角色", description = "修改用户角色,会删除之前的角色信息。")
    @PostMapping("/update/roles")
    public JsonResult updateRole(@RequestBody @Validated UpdateUserRoleIdVO data,
                                 BindingResult br) {
        try {
            return JsonResult.success(
                    baseService.updateUserRoles(data.getUserId(), data.getRoles())
            );
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        }
    }


}
