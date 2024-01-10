package com.xaaef.molly.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.entity.ChinaArea;
import com.xaaef.molly.system.service.ChinaAreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 中国行政地区 Controller
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 16:37
 */

@Slf4j
@Tag(name = "[ 通用 ] 中国行政地区")
@RestController
@AllArgsConstructor
@RequestMapping("/china/area")
public class ChinaAreaController {

    private ChinaAreaService baseService;

    @Operation(summary = "单个查询", description = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<ChinaArea> getById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @Operation(summary = "查询子节点", description = "查询所有，0.查询所有一级节点。")
    @GetMapping("/child/{parentCode}")
    public JsonResult<List<ChinaArea>> childList(@PathVariable Long parentCode) {
        if (parentCode == null || parentCode < 1) {
            parentCode = 0L;
        }
        var queryWrapper = new LambdaQueryWrapper<ChinaArea>();
        queryWrapper.eq(ChinaArea::getParentCode, parentCode);
        return JsonResult.success(baseService.list(queryWrapper));
    }


    @Operation(summary = "分页", description = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<ChinaArea>> pageQuery(SearchPO params) {
        IPage<ChinaArea> page = baseService.pageKeywords(params);
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


}
