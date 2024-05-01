package com.xaaef.molly.system.controller;


import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidUpdate;
import com.xaaef.molly.system.entity.SysConfig;
import com.xaaef.molly.system.service.SysConfigService;
import com.xaaef.molly.web.repeat.NoRepeatSubmit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/sys/config")
@AllArgsConstructor
@Tag(name = "[ 通用 ] 全局配置")
public class SysConfigController {

    public final SysConfigService baseService;


    @Operation(summary = "Id查询", description = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<SysConfig> getById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @Operation(summary = "查询所有", description = " 查询所有")
    @GetMapping("/list")
    public JsonResult<List<SysConfig>> list() {
        var list = baseService.list();
        return JsonResult.success(list);
    }


    @Operation(summary = "分页查询", description = "分页 查询")
    @GetMapping("/query")
    public JsonResult<Pagination<SysConfig>> pageQuery(SearchPO params) {
        var page = baseService.pageKeywords(params,
                List.of(SysConfig::getConfigName, SysConfig::getConfigKey)
        );
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @NoRepeatSubmit
    @Operation(summary = "新增", description = "不需要添加id")
    @PostMapping()
    public JsonResult<Boolean> save(@RequestBody @Validated(ValidCreate.class) SysConfig entity) {
        var flag = baseService.save(entity);
        return JsonResult.success(flag);
    }


    @NoRepeatSubmit
    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping()
    public JsonResult<Boolean> updateById(@RequestBody @Validated(ValidUpdate.class) SysConfig entity) {
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
