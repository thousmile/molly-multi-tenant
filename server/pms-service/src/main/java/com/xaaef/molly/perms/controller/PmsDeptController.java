package com.xaaef.molly.perms.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchParentPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.perms.service.PmsDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门 控制器
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/8 10:15
 */

@Slf4j
@Tag(name = "[ 权限 ] 部门")
@RestController
@RequestMapping("/pms/dept")
@AllArgsConstructor
public class PmsDeptController {

    private PmsDeptService baseService;


    @Operation(summary = "单个查询", description = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<PmsDept> findById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @Operation(summary = "分页", description = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<PmsDept>> pageQuery(SearchParentPO params) {
        IPage<PmsDept> page = baseService.pageKeywords(params);
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @Operation(summary = "查询所有", description = " 查询所有")
    @GetMapping("/list")
    public JsonResult<List<PmsDept>> list() {
        return JsonResult.success(baseService.list());
    }


    @Operation(summary = "查询树节点", description = "查询所有[以树节点形式展示]")
    @GetMapping("/tree")
    public JsonResult<List<Tree<Long>>> tree() {
        return JsonResult.success(baseService.treeNode());
    }


    @Operation(summary = "新增", description = "不需要添加id")
    @PostMapping()
    public JsonResult create(@RequestBody PmsDept entity) {
        try {
            baseService.save(entity);
            return JsonResult.success(entity);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping()
    public JsonResult update(@RequestBody PmsDept entity) {
        try {
            return JsonResult.success(baseService.updateById(entity));
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @Operation(summary = "删除", description = "只需要id即可")
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable("id") Long id) {
        try {
            return JsonResult.success(baseService.removeById(id));
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


}
