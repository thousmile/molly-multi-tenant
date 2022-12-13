package com.xaaef.molly.system.controller;


import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.entity.CommConfig;
import com.xaaef.molly.system.service.CommConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/comm/config")
public class CommConfigController {

    public final CommConfigService baseService;


    @GetMapping("/{id}")
    public JsonResult<String> findByKey(@PathVariable("id") String id) {
        return JsonResult.success(baseService.findValueByKey(id));
    }


    @GetMapping("/query")
    public JsonResult<Pagination<CommConfig>> findByQuery(SearchPO po) {
        var build = CommConfig.builder().configKey(po.getKeywords()).build();
        var pageRequest = PageRequest.of(po.getPageIndex(), po.getPageSize());
        var pageResult = baseService.findPage(Example.of(build), pageRequest);
        return JsonResult.success(
                pageResult.getTotalElements(),
                pageResult.getContent()
        );
    }


    @PostMapping
    public JsonResult<CommConfig> save(@RequestBody CommConfig entity) {
        return JsonResult.success(
                baseService.save(entity)
        );
    }


    @PutMapping
    public JsonResult<CommConfig> updateById(@RequestBody CommConfig entity) {
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
