package com.xaaef.molly.system.controller;


import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.system.entity.SysConfig;
import com.xaaef.molly.system.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/sys/config")
@AllArgsConstructor
@Api(tags = "[ 通用 ] 全局配置")
public class SysConfigController {

    public final SysConfigService baseService;


    @ApiOperation(value = "Id查询", notes = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<SysConfig> findById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @ApiOperation(value = "查询所有", notes = " 查询所有")
    @GetMapping("/list")
    public JsonResult<List<SysConfig>> list() {
        var list = baseService.list();
        return JsonResult.success(list);
    }


    @ApiOperation(value = "分页查询", notes = "分页 查询")
    @GetMapping("/query")
    public JsonResult<Pagination<SysConfig>> pageQuery(SearchPO params) {
        var page = baseService.pageKeywords(params,
                SysConfig::getConfigName,
                SysConfig::getConfigKey
        );
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @ApiOperation(value = "新增", notes = "不需要添加id")
    @OperateLog(title = "[全局配置] 新增", type = LogType.INSERT)
    @PostMapping()
    public JsonResult<Boolean> create(@RequestBody SysConfig entity) {
        var flag = baseService.save(entity);
        return JsonResult.success(flag);
    }


    @ApiOperation(value = "修改", notes = "修改必须要id")
    @OperateLog(title = "[全局配置] 修改", type = LogType.UPDATE)
    @PutMapping()
    public JsonResult<Boolean> update(@RequestBody SysConfig entity) {
        var flag = baseService.updateById(entity);
        return JsonResult.success(flag);
    }


    @ApiOperation(value = "删除", notes = "只需要id即可")
    @OperateLog(title = "[全局配置] 删除", type = LogType.DELETE)
    @DeleteMapping("/{id}")
    public JsonResult<Boolean> delete(@PathVariable("id") Long id) {
        var flag = baseService.removeById(id);
        return JsonResult.success(flag);
    }

}
