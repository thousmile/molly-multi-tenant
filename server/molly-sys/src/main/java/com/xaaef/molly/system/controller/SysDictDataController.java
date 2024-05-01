package com.xaaef.molly.system.controller;

import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidUpdate;
import com.xaaef.molly.system.entity.SysDictData;
import com.xaaef.molly.system.po.DictQueryPO;
import com.xaaef.molly.system.service.SysDictDataService;
import com.xaaef.molly.system.vo.DictDataVO;
import com.xaaef.molly.web.repeat.NoRepeatSubmit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典数据 控制器
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/8 10:15
 */


@Slf4j
@Tag(name = "[ 通用 ] 字典数据")
@RestController
@RequestMapping("/dict/data")
@AllArgsConstructor
public class SysDictDataController {

    private SysDictDataService baseService;

    @Operation(summary = "Id查询", description = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<SysDictData> getById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @Operation(summary = "分页查询", description = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<SysDictData>> pageQuery(DictQueryPO params) {
        var page = baseService.pageKeywords(params);
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @Operation(summary = "[Map]查询所有", description = "分页 查询所有")
    @GetMapping("/mapKeys")
    public JsonResult<Map<String, List<DictDataVO>>> mapKeys() {
        var result = baseService.mapByKeys();
        return JsonResult.success(result);
    }


    @Operation(summary = "根据Type Key查询所有", description = "根据字典type查询所有")
    @GetMapping("/type/{key}")
    public JsonResult<List<SysDictData>> listByKey(@PathVariable("key") String key) {
        return JsonResult.success(baseService.listByKey(key));
    }


    @NoRepeatSubmit
    @Operation(summary = "新增", description = "不需要添加id")
    @PostMapping()
    public JsonResult<SysDictData> save(@RequestBody @Validated(ValidCreate.class) SysDictData entity) {
        baseService.save(entity);
        return JsonResult.success(entity);
    }


    @NoRepeatSubmit
    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping()
    public JsonResult<Boolean> updateById(@RequestBody @Validated(ValidUpdate.class) SysDictData entity) {
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
