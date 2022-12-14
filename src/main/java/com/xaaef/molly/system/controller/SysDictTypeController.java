package com.xaaef.molly.system.controller;

import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.system.entity.CommDictType;
import com.xaaef.molly.system.service.CommDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Api(tags = "[ 通用 ] 字典类型")
@RestController
@RequestMapping("/dict/type")
@AllArgsConstructor
public class SysDictTypeController {


    private CommDictTypeService baseService;


    @ApiOperation(value = "单个查询", notes = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<CommDictType> findById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @ApiOperation(value = "查询所有", notes = "查询所有")
    @GetMapping("/list")
    public JsonResult<List<CommDictType>> list() {
        return JsonResult.success(baseService.list());
    }


    @ApiOperation(value = "分页", notes = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<CommDictType>> pageQuery(SearchPO params) {
        var page = baseService.pageKeywords(
                params,
                CommDictType::getTypeName,
                CommDictType::getDescription
        );
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @ApiOperation(value = "新增", notes = "不需要添加id")
    @OperateLog(title = "[字典类型]新增", type = LogType.INSERT)
    @PostMapping()
    public JsonResult<CommDictType> create(@RequestBody CommDictType entity) {
        baseService.save(entity);
        return JsonResult.success(entity);
    }


    @ApiOperation(value = "修改", notes = "修改必须要id")
    @OperateLog(title = "[字典类型]修改", type = LogType.UPDATE)
    @PutMapping()
    public JsonResult<Boolean> update(@RequestBody CommDictType entity) {
        var flag = baseService.updateById(entity);
        return JsonResult.success(flag);
    }


    @ApiOperation(value = "删除", notes = "只需要id即可")
    @OperateLog(title = "[字典类型]删除", type = LogType.DELETE)
    @DeleteMapping("/{id}")
    public JsonResult<Boolean> delete(@PathVariable("id") Long id) {
        var flag = baseService.removeById(id);
        return JsonResult.success(flag);
    }


}
