package com.xaaef.molly.perms.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.po.SearchParentPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.perms.entity.PmsDept;
import com.xaaef.molly.perms.service.PmsDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "[ 权限 ] 部门")
@RestController
@RequestMapping("/pms/dept")
@AllArgsConstructor
public class PmsDeptController {

    private PmsDeptService baseService;


    @ApiOperation(value = "单个查询", notes = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<PmsDept> findById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @ApiOperation(value = "分页", notes = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<PmsDept>> pageQuery(SearchParentPO params) {
        IPage<PmsDept> page = baseService.pageKeywords(params);
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @ApiOperation(value = "查询所有", notes = " 查询所有")
    @GetMapping("/list")
    public JsonResult<List<PmsDept>> list() {
        return JsonResult.success(baseService.list());
    }


    @ApiOperation(value = "查询树节点", notes = "查询所有[以树节点形式展示]")
    @GetMapping("/tree")
    public JsonResult<List<Tree<Long>>> tree() {
        return JsonResult.success(baseService.treeNode());
    }


    @ApiOperation(value = "新增", notes = "不需要添加id")
    @OperateLog(title = "[部门]新增", type = LogType.INSERT)
    @PostMapping()
    public JsonResult create(@RequestBody PmsDept entity) {
        try {
            baseService.save(entity);
            return JsonResult.success(entity);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "修改", notes = "修改必须要id")
    @OperateLog(title = "[部门]修改", type = LogType.UPDATE)
    @PutMapping()
    public JsonResult update(@RequestBody PmsDept entity) {
        try {
            return JsonResult.success(baseService.updateById(entity));
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "删除", notes = "只需要id即可")
    @OperateLog(title = "[部门]删除", type = LogType.DELETE)
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable("id") Long id) {
        try {
            return JsonResult.success(baseService.removeById(id));
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


}
