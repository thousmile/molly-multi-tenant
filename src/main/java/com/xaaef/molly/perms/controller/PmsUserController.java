package com.xaaef.molly.perms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.po.UserQueryPO;
import com.xaaef.molly.perms.service.PmsUserService;
import com.xaaef.molly.perms.vo.ResetPasswordVO;
import com.xaaef.molly.perms.vo.UpdatePasswordVO;
import com.xaaef.molly.perms.vo.UpdateUserRoleIdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/pms/user")
@Api(tags = "[ 权限 ] 用户")
@AllArgsConstructor
public class PmsUserController {

    private final PmsUserService baseService;


    @ApiOperation(value = "分页查询", notes = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<PmsUser>> pageQuery(UserQueryPO params) {
        IPage<PmsUser> page = baseService.pageKeywords(params);
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @ApiOperation(value = "新增", notes = "不需要添加id")
    @OperateLog(title = "[用户管理]新增", type = LogType.INSERT)
    @PostMapping()
    public JsonResult<PmsUser> create(@RequestBody PmsUser entity) {
        try {
            baseService.save(entity);
            return JsonResult.success(entity);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage(), PmsUser.class);
        }
    }



    @ApiOperation(value = "修改", notes = "修改必须要id")
    @OperateLog(title = "[用户]修改", type = LogType.UPDATE)
    @PutMapping()
    public JsonResult update(@RequestBody PmsUser entity) {
        try {
            boolean byId = baseService.updateById(entity);
            return JsonResult.success(byId);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "删除", notes = "修改必须要id")
    @OperateLog(title = "[用户]删除", type = LogType.UPDATE)
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable Long id) {
        try {
            boolean b = baseService.removeById(id);
            return JsonResult.success(b);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }



    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @OperateLog(title = "[用户]修改用户密码", type = LogType.UPDATE)
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


    @ApiOperation(value = "重置用户密码", notes = "重置用户密码")
    @OperateLog(title = "[用户]重置用户密码", type = LogType.UPDATE)
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


    @ApiOperation(value = "修改用户角色", notes = "修改用户角色,会删除之前的角色信息。")
    @OperateLog(title = "[用户]修改用户角色", type = LogType.UPDATE)
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
