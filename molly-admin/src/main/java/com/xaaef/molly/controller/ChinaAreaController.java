package com.xaaef.molly.controller;

import com.xaaef.molly.common.util.JsonResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 中国行政地区 Controller
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 16:37
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/china/area")
public class ChinaAreaController {

    @GetMapping()
    public JsonResult<String> findById() {
        return JsonResult.success();
    }


    @GetMapping("/hello")
    public JsonResult<String> hello() {
        return JsonResult.success();
    }

}
