package com.xaaef.molly.system.controller;


import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.system.entity.SysConfig;
import com.xaaef.molly.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public JsonResult<SysConfig> findById(@PathVariable("id") Long id) {
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
                SysConfig::getConfigName,
                SysConfig::getConfigKey
        );
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @Operation(summary = "新增", description = "不需要添加id")
    @OperateLog(title = "[全局配置] 新增", type = LogType.INSERT)
    @PostMapping()
    public JsonResult<Boolean> create(@RequestBody SysConfig entity) {
        var flag = baseService.save(entity);
        return JsonResult.success(flag);
    }


    @Operation(summary = "修改", description = "修改必须要id")
    @OperateLog(title = "[全局配置] 修改", type = LogType.UPDATE)
    @PutMapping()
    public JsonResult<Boolean> update(@RequestBody SysConfig entity) {
        var flag = baseService.updateById(entity);
        return JsonResult.success(flag);
    }


    @Operation(summary = "删除", description = "只需要id即可")
    @OperateLog(title = "[全局配置] 删除", type = LogType.DELETE)
    @DeleteMapping("/{id}")
    public JsonResult<Boolean> delete(@PathVariable("id") Long id) {
        var flag = baseService.removeById(id);
        return JsonResult.success(flag);
    }

}
