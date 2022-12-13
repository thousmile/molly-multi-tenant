package com.xaaef.molly.system.controller;

import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.po.CreateTenantPO;
import com.xaaef.molly.system.po.TenantCreatedSuccessVO;
import com.xaaef.molly.system.service.SysTenantService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/sys/tenant")
public class SysTenantController {

    public final SysTenantService baseService;


    @GetMapping("/{id}")
    public JsonResult<SysTenant> findById(@PathVariable("id") String id) {
        return JsonResult.success(baseService.getById(id));
    }


    @GetMapping("/exist/{id}")
    public JsonResult<Boolean> existById(@PathVariable("id") String id) {
        return JsonResult.success(baseService.existsById(id));
    }


    @GetMapping("/query")
    public JsonResult<Pagination<SysTenant>> findByQuery(SearchPO po) {
        var build = SysTenant.builder().name(po.getKeywords()).build();
        var pageRequest = PageRequest.of(po.getPageIndex(), po.getPageSize());
        var pageResult = baseService.findPage(Example.of(build), pageRequest);
        return JsonResult.success(
                pageResult.getTotalElements(),
                pageResult.getContent()
        );
    }


    @PostMapping
    public JsonResult<TenantCreatedSuccessVO> save(@Validated @RequestBody CreateTenantPO po, BindingResult br) {
        try {
            return JsonResult.success(
                    baseService.create(po)
            );
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage(), TenantCreatedSuccessVO.class);
        }
    }


    @PutMapping
    public JsonResult<SysTenant> updateById(@RequestBody SysTenant tenant) {
        return JsonResult.success(
                baseService.updateById(tenant)
        );
    }


    @DeleteMapping("/{id}")
    public JsonResult<String> deleteById(@PathVariable("id") String id) {
        baseService.deleteById(id);
        return JsonResult.success();
    }


}
