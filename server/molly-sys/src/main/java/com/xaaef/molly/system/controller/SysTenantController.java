package com.xaaef.molly.system.controller;

import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.po.CreateTenantPO;
import com.xaaef.molly.system.po.TenantCreatedSuccessVO;
import com.xaaef.molly.system.service.SysTenantService;
import com.xaaef.molly.system.vo.UpdateTenantTemplateIdVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/sys/tenant")
@Tag(name = "[ 系统 ] 租户管理")
public class SysTenantController {

    public final SysTenantService baseService;


    @Operation(summary = "Id查询", description = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<SysTenant> findById(@PathVariable("id") String id) {
        return JsonResult.success(baseService.getById(id));
    }


    @Operation(summary = "租户ID是否存在", description = "判断租户ID是否存在")
    @GetMapping("/exist/{id}")
    public JsonResult<Boolean> existById(@PathVariable("id") String id) {
        return JsonResult.success(baseService.exist(SysTenant::getTenantId, id));
    }


    @Operation(summary = "分页查询", description = "分页 搜索查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<SysTenant>> pageQuery(SearchPO po) {
        var pageResult = baseService.pageKeywords(po);
        return JsonResult.success(
                pageResult.getTotal(),
                pageResult.getRecords()
        );
    }


    @Operation(summary = "简单分页查询", description = "简单分页 搜索查询所有")
    @GetMapping("/simple/query")
    public JsonResult<Pagination<SysTenant>> simplePageQuery(SearchPO po) {
        var pageResult = baseService.simplePageKeywords(po);
        return JsonResult.success(
                pageResult.getTotal(),
                pageResult.getRecords()
        );
    }


    @Operation(summary = "新增租户", description = "新增租户")
    @PostMapping
    public JsonResult<TenantCreatedSuccessVO> save(@Validated @RequestBody CreateTenantPO po, BindingResult br) {
        try {
            return JsonResult.success(baseService.create(po));
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage(), TenantCreatedSuccessVO.class);
        }
    }


    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysTenant tenant) {
        var flag = baseService.updateById(tenant);
        return JsonResult.success(flag);
    }


    @Operation(summary = "删除", description = "只需要id即可")
    @DeleteMapping("/{id}")
    public JsonResult<String> deleteById(@PathVariable("id") String id) {
        baseService.removeById(id);
        return JsonResult.success();
    }


    @Operation(summary = "修改权限模板", description = "修改租户权限")
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
