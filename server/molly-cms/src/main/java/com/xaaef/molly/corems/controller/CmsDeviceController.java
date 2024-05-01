package com.xaaef.molly.corems.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchParentPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.valid.ValidCreate;
import com.xaaef.molly.common.valid.ValidUpdate;
import com.xaaef.molly.corems.entity.CmsDevice;
import com.xaaef.molly.corems.service.CmsDeviceService;
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
 * 设备 控制器
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/8 10:15
 */

@Slf4j
@Tag(name = "[ 核心 ] 设备")
@RestController
@RequestMapping("/cms/device")
@AllArgsConstructor
public class CmsDeviceController {

    private CmsDeviceService baseService;


    @Operation(summary = "单个查询", description = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<CmsDevice> findById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @Operation(summary = "分页", description = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<CmsDevice>> pageQuery(SearchParentPO params) {
        IPage<CmsDevice> page = baseService.pageKeywords(params,
                List.of(CmsDevice::getDeviceName));
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @Operation(summary = "查询所有", description = " 查询所有")
    @GetMapping("/list")
    public JsonResult<List<CmsDevice>> list() {
        return JsonResult.success(baseService.list());
    }


    @NoRepeatSubmit
    @Operation(summary = "新增", description = "不需要添加id")
    @PostMapping()
    public JsonResult<Boolean> create(@RequestBody @Validated(ValidCreate.class) CmsDevice entity) {
        var flag = baseService.save(entity);
        return JsonResult.success(flag);
    }


    @NoRepeatSubmit
    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping()
    public JsonResult<Boolean> updateById(@RequestBody @Validated(ValidUpdate.class) CmsDevice entity) {
        return JsonResult.success(baseService.updateById(entity));
    }


    @NoRepeatSubmit
    @Operation(summary = "删除", description = "只需要id即可")
    @DeleteMapping("/{id}")
    public JsonResult<Boolean> removeById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.removeById(id));
    }


}
