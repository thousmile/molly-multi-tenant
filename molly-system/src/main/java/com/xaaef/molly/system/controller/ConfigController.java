package com.xaaef.molly.system.controller;


import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.entity.SysConfig;
import com.xaaef.molly.system.service.SysConfigService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/sys/config")
public class ConfigController {

    public final SysConfigService baseService;


    @GetMapping("/{id}")
    public JsonResult<String> findByKey(@PathVariable("id") String id) {
        return JsonResult.success(baseService.findValueByKey(id));
    }


    @GetMapping("/query")
    public JsonResult<Pagination<SysConfig>> findByQuery(SearchPO po) {
        var build = SysConfig.builder().configKey(po.getKeywords()).build();
        var pageRequest = PageRequest.of(po.getPageIndex(), po.getPageSize());
        var pageResult = baseService.findPage(Example.of(build), pageRequest);
        return JsonResult.success(
                pageResult.getTotalElements(),
                pageResult.getContent()
        );
    }


    @PostMapping
    public JsonResult<SysConfig> save(@RequestBody SysConfig entity) {
        return JsonResult.success(
                baseService.save(entity)
        );
    }


    @PutMapping
    public JsonResult<SysConfig> updateById(@RequestBody SysConfig entity) {
        return JsonResult.success(
                baseService.updateById(entity)
        );
    }


    @DeleteMapping("/{id}")
    public JsonResult<String> deleteById(@PathVariable("id") Long id) {
        baseService.deleteById(id);
        return JsonResult.success();
    }


}
