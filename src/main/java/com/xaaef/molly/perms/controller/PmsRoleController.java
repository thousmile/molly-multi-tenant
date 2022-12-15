package com.xaaef.molly.perms.controller;

import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.service.PmsRoleService;
import com.xaaef.molly.system.vo.BindingMenusVO;
import com.xaaef.molly.system.vo.UpdateMenusVO;
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
@RequestMapping("/pms/role")
@Api(tags = "[ 权限 ] 角色")
@AllArgsConstructor
public class PmsRoleController {

    private final PmsRoleService baseService;


    @ApiOperation(value = "单个查询", notes = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<PmsRole> findById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @ApiOperation(value = "查询所有", notes = "查询所有")
    @GetMapping("/list")
    public JsonResult<List<PmsRole>> list() {
        return JsonResult.success(baseService.list());
    }


    @ApiOperation(value = "分页", notes = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<PmsRole>> pageQuery(SearchPO params) {
        var page = baseService.pageKeywords(
                params,
                PmsRole::getRoleName,
                PmsRole::getDescription
        );
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @ApiOperation(value = "新增", notes = "不需要添加id")
    @OperateLog(title = "[角色]新增", type = LogType.INSERT)
    @PostMapping()
    public JsonResult create(@RequestBody PmsRole entity) {
        baseService.save(entity);
        return JsonResult.success(entity);
    }


    @ApiOperation(value = "修改", notes = "修改必须要id")
    @OperateLog(title = "[角色]修改", type = LogType.UPDATE)
    @PutMapping()
    public JsonResult<Boolean> update(@RequestBody PmsRole entity) {
        boolean b = baseService.updateById(entity);
        return JsonResult.success(b);
    }


    @ApiOperation(value = "删除", notes = "只需要id即可")
    @OperateLog(title = "[角色]删除", type = LogType.DELETE)
    @DeleteMapping("/{id}")
    public JsonResult<Boolean> delete(@PathVariable("id") Long id) {
        boolean b = baseService.removeById(id);
        return JsonResult.success(b);
    }


    @ApiOperation(value = "拥有的权限", notes = "查询角色拥有的权限！")
    @GetMapping("/menus/{roleId}")
    public JsonResult<UpdateMenusVO> selectHaveMenus(@PathVariable Long roleId) {
        var data = baseService.listHaveMenus(roleId);
        return JsonResult.success(data);
    }


    @ApiOperation(value = "绑定菜单", notes = "会删除原有的菜单！")
    @OperateLog(title = "[角色]绑定菜单", type = LogType.UPDATE)
    @PostMapping("/menus")
    public JsonResult<Boolean> bindingMenus(@RequestBody @Validated BindingMenusVO entity,
                                            BindingResult br) {
        boolean b = baseService.updateMenus(entity.getId(), entity.getMenus());
        return JsonResult.success(b);
    }

}
