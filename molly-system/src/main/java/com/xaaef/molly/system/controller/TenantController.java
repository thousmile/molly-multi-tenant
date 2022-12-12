package com.xaaef.molly.system.controller;


import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.service.SysTenantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/sys/tenant")
public class TenantController {

    public final SysTenantService baseService;


    @GetMapping("/{id}")
    public JsonResult<SysTenant> findById(@PathVariable("id") String id) {
        return JsonResult.success(baseService.getById(id));
    }


    @GetMapping("/exist/{id}")
    public JsonResult<Boolean> existById(@PathVariable("id") String id) {
        return JsonResult.success(baseService.existsById(id));
    }


    @PostMapping
    public JsonResult<SysTenant> save(@RequestBody SysTenant tenant) {
        return JsonResult.success(
                baseService.save(tenant)
        );
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
