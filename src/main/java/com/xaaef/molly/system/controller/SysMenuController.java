package com.xaaef.molly.system.controller;

import cn.hutool.core.lang.tree.Tree;
import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.system.entity.SysMenu;
import com.xaaef.molly.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 菜单 控制器
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/8 10:15
 */

@Slf4j
@Tag(name = "[ 权限 ] 菜单管理")
@RestController
@RequestMapping("/sys/menu")
@AllArgsConstructor
public class SysMenuController {

    private SysMenuService baseService;

    @Operation(summary = "单个查询", description = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<SysMenu> findById(@PathVariable("id") Long id) {
        log.debug("get ID : {}", id);
        return JsonResult.success(baseService.getById(id));
    }


    @Operation(summary = "分页", description = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<SysMenu>> pageQuery(SearchPO params) {
        var page = baseService.pageKeywords(
                params,
                SysMenu::getMenuName, SysMenu::getPerms
        );
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @Operation(summary = "查询所有", description = "查询所有")
    @GetMapping("/list")
    public JsonResult<List<SysMenu>> list() {
        List<SysMenu> list = baseService.list();
        list.sort(Comparator.comparing(SysMenu::getSort));
        return JsonResult.success(list);
    }


    @Operation(summary = "查询树节点", description = "查询所有[以树节点形式展示]")
    @GetMapping("/tree")
    public JsonResult<List<Tree<Long>>> tree() {
        return JsonResult.success(baseService.tree());
    }


    /**
     * 根据当前用户所属的类型
     * 租户用户：租户菜单和公共菜单
     * 系统用户: 系统菜单和公共菜单
     * <p>
     * 具体根据 菜单中的 target 属性
     */
    @Operation(summary = "可用的菜单", description = "根据当前用户查询此租户的全部可用菜单[以树节点形式展示]")
    @GetMapping("/available")
    public JsonResult<List<Tree<Long>>> available() {
        return JsonResult.success(
                baseService.treeCurrentUserAvailable()
        );
    }


    @Operation(summary = "租户可用的菜单", description = "租户可用的全部菜单[以树节点形式展示]")
    @GetMapping("/tenant/available")
    public JsonResult<List<Tree<Long>>> tenantAvailable() {
        return JsonResult.success(
                baseService.treeTenantAvailable()
        );
    }


    @Operation(summary = "新增", description = "不需要添加id")
    @OperateLog(title = "[菜单]新增", type = LogType.INSERT)
    @PostMapping()
    public JsonResult create(@RequestBody SysMenu entity) {
        try {
            baseService.save(entity);
            return JsonResult.success(entity);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @Operation(summary = "修改", description = "修改必须要id")
    @OperateLog(title = "[菜单]修改", type = LogType.UPDATE)
    @PutMapping()
    public JsonResult update(@RequestBody SysMenu entity) {
        try {
            boolean byId = baseService.updateById(entity);
            return JsonResult.success(byId);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @Operation(summary = "删除", description = "只需要id即可")
    @OperateLog(title = "[菜单]删除", type = LogType.DELETE)
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable("id") Long id) {
        try {
            boolean byId = baseService.removeById(id);
            return JsonResult.success(byId);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


}
