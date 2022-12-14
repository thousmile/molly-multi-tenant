package com.xaaef.molly.system.controller;

import com.xaaef.molly.common.domain.Pagination;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.core.log.LogType;
import com.xaaef.molly.core.log.OperateLog;
import com.xaaef.molly.system.entity.CommDictData;
import com.xaaef.molly.system.po.DictQueryPO;
import com.xaaef.molly.system.service.CommDictDataService;
import com.xaaef.molly.system.vo.DictDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典数据 控制器
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/8 10:15
 */


@Slf4j
@Api(tags = "[ 通用 ] 字典数据")
@RestController
@RequestMapping("/dict/data")
@AllArgsConstructor
public class CommDictDataController {

    private CommDictDataService baseService;

    @ApiOperation(value = "Id查询", notes = "根据Id查询")
    @GetMapping("/{id}")
    public JsonResult<CommDictData> findById(@PathVariable("id") Long id) {
        return JsonResult.success(baseService.getById(id));
    }


    @ApiOperation(value = "分页查询", notes = "分页 查询所有")
    @GetMapping("/query")
    public JsonResult<Pagination<CommDictData>> pageQuery(DictQueryPO params) {
        var page = baseService.pageKeywords(params);
        return JsonResult.success(page.getTotal(), page.getRecords());
    }


    @ApiOperation(value = "[Map]查询所有", notes = "分页 查询所有")
    @GetMapping("/mapKeys")
    public JsonResult<Map<String, List<DictDataVO>>> mapKeys() {
        var result = baseService.mapByKeys();
        return JsonResult.success(result);
    }


    @ApiOperation(value = "根据Type Key查询所有", notes = "根据字典type查询所有")
    @GetMapping("/type/{key}")
    public JsonResult<List<CommDictData>> findTypeKey(@PathVariable("key") String key) {
        return JsonResult.success(baseService.listByKey(key));
    }


    @ApiOperation(value = "新增", notes = "不需要添加id")
    @OperateLog(title = "[字典数据]新增", type = LogType.INSERT)
    @PostMapping()
    public JsonResult<CommDictData> create(@RequestBody CommDictData entity) {
        baseService.save(entity);
        return JsonResult.success(entity);
    }


    @ApiOperation(value = "修改", notes = "修改必须要id")
    @OperateLog(title = "[字典数据]修改", type = LogType.UPDATE)
    @PutMapping()
    public JsonResult<Boolean> update(@RequestBody CommDictData entity) {
        boolean b = baseService.updateById(entity);
        return JsonResult.success(b);
    }


    @ApiOperation(value = "删除", notes = "只需要id即可")
    @OperateLog(title = "[字典数据]删除", type = LogType.DELETE)
    @DeleteMapping("/{id}")
    public JsonResult<Boolean> delete(@PathVariable("id") Long id) {
        boolean b = baseService.removeById(id);
        return JsonResult.success(b);
    }

}
