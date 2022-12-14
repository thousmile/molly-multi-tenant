package com.xaaef.molly.system.controller;

import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.po.CreateTenantPO;
import com.xaaef.molly.system.po.TenantCreatedSuccessVO;
import com.xaaef.molly.system.service.SysTenantService;
import com.xaaef.molly.system.vo.UpdateTenantTemplateIdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/sys/tenant")
@Api(tags = "[ 系统 ] 租户")
public class SysTenantController {

    public final SysTenantService baseService;


    @ApiOperation(value = "Id查询", notes = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<SysTenant> findById(@PathVariable("id") String id) {
        return JsonResult.success(baseService.getById(id));
    }


    @ApiOperation(value = "租户ID是否存在", notes = "判断租户ID是否存在")
    @GetMapping("/exist/{id}")
    public JsonResult<Boolean> existById(@PathVariable("id") String id) {
        return JsonResult.success(baseService.exist(SysTenant::getTenantId, id));
    }


    @ApiOperation(value = "分页查询", notes = "分页 搜索查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<SysTenant>> pageQuery(SearchPO po) {
        var pageResult = baseService.pageKeywords(po);
        return JsonResult.success(
                pageResult.getTotal(),
                pageResult.getRecords()
        );
    }


    @ApiOperation(value = "简单分页查询", notes = "简单分页 搜索查询所有")
    @GetMapping("/simple/query")
    public JsonResult<Pagination<SysTenant>> simplePageQuery(SearchPO po) {
        var pageResult = baseService.simplePageKeywords(po);
        return JsonResult.success(
                pageResult.getTotal(),
                pageResult.getRecords()
        );
    }


    @ApiOperation(value = "新增租户", notes = "新增租户")
    @OperateLog(title = "[租户] 新增", type = LogType.INSERT)
    @PostMapping
    public JsonResult<TenantCreatedSuccessVO> save(@Validated @RequestBody CreateTenantPO po, BindingResult br) {
        try {
            return JsonResult.success(baseService.create(po));
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage(), TenantCreatedSuccessVO.class);
        }
    }


    @ApiOperation(value = "修改", notes = "修改必须要id")
    @OperateLog(title = "[租户] 修改", type = LogType.UPDATE)
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysTenant tenant) {
        var flag = baseService.updateById(tenant);
        return JsonResult.success(flag);
    }


    @ApiOperation(value = "删除", notes = "只需要id即可")
    @OperateLog(title = "[租户] 删除", type = LogType.DELETE)
    @DeleteMapping("/{id}")
    public JsonResult<String> deleteById(@PathVariable("id") String id) {
        baseService.removeById(id);
        return JsonResult.success();
    }


    @ApiOperation(value = "关联权限", notes = "修改租户权限")
    @OperateLog(title = "[租户] 修改权限模板", type = LogType.UPDATE)
    @PostMapping("/update/templates")
    public JsonResult<String> updateTemplateId(@RequestBody @Validated UpdateTenantTemplateIdVO params,
                                               BindingResult br) {
        try {
            baseService.updateTemplate(params.getTenantId(), params.getTemplateIds());
            return JsonResult.success();
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


}
