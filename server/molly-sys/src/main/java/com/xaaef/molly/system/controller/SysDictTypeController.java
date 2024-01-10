package com.xaaef.molly.system.controller;

import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.entity.SysDictType;
import com.xaaef.molly.system.service.SysDictTypeService;
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
 * 字典类型 控制器
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/8 10:15
 */

@Slf4j
@Tag(name = "[ 通用 ] 字典类型")
@RestController
@RequestMapping("/dict/type")
@AllArgsConstructor
public class SysDictTypeController {


    private SysDictTypeService baseService;


    @Operation(summary = "单个查询", description = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<SysDictType> getById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @Operation(summary = "查询所有", description = "查询所有")
    @GetMapping("/list")
    public JsonResult<List<SysDictType>> list() {
        return JsonResult.success(baseService.list());
    }


    @Operation(summary = "分页", description = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<SysDictType>> pageQuery(SearchPO params) {
        var page = baseService.pageKeywords(
                params,
                List.of(SysDictType::getTypeName, SysDictType::getDescription)
        );
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @NoRepeatSubmit
    @Operation(summary = "新增", description = "不需要添加id")
    @PostMapping()
    public JsonResult<SysDictType> save(@RequestBody @Validated(SysDictType.ValidCreate.class) SysDictType entity) {
        baseService.save(entity);
        return JsonResult.success(entity);
    }


    @NoRepeatSubmit
    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping()
    public JsonResult<Boolean> updateById(@RequestBody @Validated(SysDictType.ValidUpdate.class) SysDictType entity) {
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


}
