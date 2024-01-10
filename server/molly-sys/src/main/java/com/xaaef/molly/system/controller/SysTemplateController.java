package com.xaaef.molly.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.entity.SysTemplate;
import com.xaaef.molly.system.service.SysTemplateService;
import com.xaaef.molly.system.vo.BindingMenusVO;
import com.xaaef.molly.system.vo.UpdateMenusVO;
import com.xaaef.molly.web.repeat.NoRepeatSubmit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 租户权限模板 控制器
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/8 10:15
 */


@Slf4j
@Tag(name = "[ 系统 ] 租户权限模板")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/template")
public class SysTemplateController {

    private final SysTemplateService baseService;


    @Operation(summary = "单个查询", description = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<SysTemplate> getById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @Operation(summary = "查询所有", description = " 查询所有")
    @GetMapping("/list")
    public JsonResult<List<SysTemplate>> list() {
        var list = baseService.list();
        return JsonResult.success(list);
    }


    @Operation(summary = "分页", description = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<SysTemplate>> pageQuery(SearchPO params) {
        IPage<SysTemplate> page = baseService.pageKeywords(params,
                List.of(SysTemplate::getName, SysTemplate::getCreateTime));
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @NoRepeatSubmit
    @Operation(summary = "新增", description = "不需要添加id")
    @PostMapping()
    public JsonResult<Boolean> save(@RequestBody @Validated(SysTemplate.ValidCreate.class) SysTemplate entity) {
        var save = baseService.save(entity);
        return JsonResult.success(save);
    }


    @NoRepeatSubmit
    @Operation(summary = "修改", description = "修改必须要id")
    @PutMapping()
    public JsonResult<Boolean> updateById(@RequestBody @Validated(SysTemplate.ValidUpdate.class) SysTemplate entity) {
        var flag = baseService.updateById(entity);
        return JsonResult.success(flag);
    }


    @NoRepeatSubmit
    @Operation(summary = "删除", description = "只需要id即可")
    @DeleteMapping("/{id}")
    public JsonResult<Boolean> removeById(@PathVariable Long id) {
        var flag = baseService.removeById(id);
        return JsonResult.success(flag);
    }


    @Operation(summary = "查询拥有的权限", description = "查询模板拥有的权限！")
    @GetMapping("/menus/{id}")
    public JsonResult<UpdateMenusVO> listHaveMenus(@PathVariable Long id) {
        return JsonResult.success(baseService.listHaveMenus(id));
    }


    @NoRepeatSubmit
    @Operation(summary = "修改菜单列表", description = "修改权限模板的菜单列表")
    @PostMapping("/menus")
    public JsonResult<Boolean> updateMenus(@RequestBody @Validated BindingMenusVO params, BindingResult br) {
        var flag = baseService.updateMenus(params.getId(), params.getMenus());
        return JsonResult.success(flag);
    }


}
