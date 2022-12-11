package com.xaaef.molly.system.controller;


import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.entity.SysTenant;
import com.xaaef.molly.system.service.SysTenantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping("/sys/tenant")
public class TenantController {

    public final SysTenantService tenantService;


    @PostMapping
    public JsonResult<SysTenant> save(@RequestBody SysTenant tenant) {
        return JsonResult.success(
                tenantService.save(tenant)
        );
    }


}
