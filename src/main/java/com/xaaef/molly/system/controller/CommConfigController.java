package com.xaaef.molly.system.controller;


import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.system.entity.CommConfig;
import com.xaaef.molly.system.service.CommConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/comm/config")
@AllArgsConstructor
@Api(tags = "[ 通用 ] 全局配置")
public class CommConfigController {

    public final CommConfigService baseService;


    @ApiOperation(value = "Id查询", notes = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<CommConfig> findById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @ApiOperation(value = "查询所有", notes = " 查询所有")
    @GetMapping("/list")
    public JsonResult<List<CommConfig>> list() {
        var list = baseService.list();
        return JsonResult.success(list);
    }


    @ApiOperation(value = "分页查询", notes = "分页 查询")
    @GetMapping("/query")
    public JsonResult<Pagination<CommConfig>> pageQuery(SearchPO params) {
        var page = baseService.pageKeywords(params,
                CommConfig::getConfigName,
                CommConfig::getConfigKey
        );
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @ApiOperation(value = "新增", notes = "不需要添加id")
    @OperateLog(title = "[全局配置] 新增", type = LogType.INSERT)
    @PostMapping()
    public JsonResult<Boolean> create(@RequestBody CommConfig entity) {
        var flag = baseService.save(entity);
        return JsonResult.success(flag);
    }


    @ApiOperation(value = "修改", notes = "修改必须要id")
    @OperateLog(title = "[全局配置] 修改", type = LogType.UPDATE)
    @PutMapping()
    public JsonResult<Boolean> update(@RequestBody CommConfig entity) {
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
