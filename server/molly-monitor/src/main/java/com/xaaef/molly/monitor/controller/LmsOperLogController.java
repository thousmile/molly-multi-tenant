package com.xaaef.molly.monitor.controller;

import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.monitor.entity.LmsOperLog;
import com.xaaef.molly.monitor.service.LmsOperLogService;
import com.xaaef.molly.web.repeat.NoRepeatSubmit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


/**
 * <p>
 * 监控 操作日志
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 3.0
 */


@Slf4j
@RestController
@RequestMapping("/lms/oper")
@Tag(name = "[ 监控 ] 操作日志")
@AllArgsConstructor
public class LmsOperLogController {

    private final LmsOperLogService baseService;


    @Operation(summary = "分页", description = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<LmsOperLog>> pageQuery(SearchPO params) {
        var page = baseService.pageKeywords(params);
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @NoRepeatSubmit
    @Operation(summary = "批量删除", description = "只需要id即可")
    @PostMapping()
    public JsonResult<Boolean> delete(@RequestBody Set<String> ids) {
        boolean b = baseService.removeBatchByIds(ids);
        return JsonResult.success(b);
    }


}
