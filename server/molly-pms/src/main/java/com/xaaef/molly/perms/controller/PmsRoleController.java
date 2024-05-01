package com.xaaef.molly.perms.controller;

import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidUpdate;
import com.xaaef.molly.perms.entity.PmsRole;
import com.xaaef.molly.perms.po.BindingMenusVO;
import com.xaaef.molly.perms.service.PmsRoleService;
import com.xaaef.molly.perms.vo.UpdateMenusVO;
import com.xaaef.molly.web.repeat.NoRepeatSubmit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "[ 权限 ] 角色")
@AllArgsConstructor
public class PmsRoleController {

    private final PmsRoleService baseService;


    @Operation(summary = "单个查询", description = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<PmsRole> getById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @Operation(summary = "查询所有", description = "查询所有")
    @GetMapping("/list")
    public JsonResult<List<PmsRole>> list() {
        return JsonResult.success(baseService.list());
    }


    @Operation(summary = "分页", description = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<PmsRole>> pageQuery(SearchPO params) {
        var page = baseService.pageKeywords(
                params, List.of(PmsRole::getRoleName, PmsRole::getDescription)
        );
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @NoRepeatSubmit
    @Operation(summary = "新增", description = "不需要添加id")
    @PostMapping()
    public JsonResult<Boolean> save(@RequestBody @Validated({ValidCreate.class}) PmsRole entity) {
        var flag = baseService.save(entity);
        return JsonResult.success(flag);
    }


    @NoRepeatSubmit
    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping()
    public JsonResult<Boolean> updateById(@RequestBody @Validated({ValidUpdate.class}) PmsRole entity) {
        var flag = baseService.updateById(entity);
        return JsonResult.success(flag);
    }


    @NoRepeatSubmit
    @Operation(summary = "删除", description = "只需要id即可")
    @DeleteMapping("/{id}")
    public JsonResult<Boolean> removeById(@PathVariable("id") Long id) {
        var flag = baseService.removeById(id);
        return JsonResult.success(flag);
    }


    @Operation(summary = "拥有的菜单", description = "查询角色拥有的菜单！")
    @GetMapping("/menus/{roleId}")
    public JsonResult<UpdateMenusVO> listHaveMenus(@PathVariable Long roleId) {
        var data = baseService.listHaveMenus(roleId);
        return JsonResult.success(data);
    }


    @NoRepeatSubmit
    @Operation(summary = "关联菜单", description = "会删除原有的菜单！")
    @PostMapping("/menus")
    public JsonResult<Boolean> bindingMenus(@RequestBody @Validated BindingMenusVO entity) {
        var flag = baseService.updateMenus(entity.getId(), entity.getMenus());
        return JsonResult.success(flag);
    }

}
