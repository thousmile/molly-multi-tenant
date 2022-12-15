package com.xaaef.molly.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.po.SearchPO;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.system.entity.SysTemplate;
import com.xaaef.molly.system.service.SysTemplateService;
import com.xaaef.molly.system.vo.BindingMenusVO;
import com.xaaef.molly.system.vo.UpdateMenusVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "[ 系统 ] 租户权限模板")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/template")
public class SysTemplateController {

    private final SysTemplateService baseService;


    @ApiOperation(value = "单个查询", notes = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<SysTemplate> findById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @ApiOperation(value = "分页", notes = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<SysTemplate>> pageQuery(SearchPO params) {
        IPage<SysTemplate> page = baseService.pageKeywords(params,
                SysTemplate::getName, SysTemplate::getCreateTime);
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @ApiOperation(value = "新增", notes = "不需要添加id")
    @OperateLog(title = "[权限模板] 新增", type = LogType.INSERT)
    @PostMapping()
    public JsonResult create(@RequestBody SysTemplate entity) {
        try {
            var save = baseService.save(entity);
            return JsonResult.success(save);
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "修改", notes = "修改必须要id")
    @OperateLog(title = "[权限模板] 修改", type = LogType.UPDATE)
    @PutMapping()
    public JsonResult<String> update(@RequestBody SysTemplate entity) {
        try {
            baseService.updateById(entity);
            return JsonResult.success();
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "删除", notes = "只需要id即可")
    @OperateLog(title = "[权限模板] 删除", type = LogType.DELETE)
    @DeleteMapping("/{id}")
    public JsonResult<String> delete(@PathVariable("id") Integer id) {
        try {
            baseService.removeById(id);
            return JsonResult.success();
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "查询拥有的权限", notes = "查询模板拥有的权限！")
    @GetMapping("/menus/{templateId}")
    public JsonResult<UpdateMenusVO> selectHaveMenus(@PathVariable Long templateId) {
        return JsonResult.success(baseService.listHaveMenus(templateId));
    }


    @ApiOperation(value = "修改菜单列表", notes = "修改权限模板的菜单列表")
    @OperateLog(title = "[权限模板]修改菜单列表", type = LogType.UPDATE)
    @PostMapping("/menus")
    public JsonResult<String> updateMenusId(@RequestBody @Validated BindingMenusVO params, BindingResult br) {
        try {
            baseService.updateMenus(params.getId(), params.getMenus());
            return JsonResult.success();
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


}
