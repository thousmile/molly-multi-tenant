package com.xaaef.molly.perms.controller;

import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.perms.entity.PmsUser;
import com.xaaef.molly.perms.service.PmsUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 3.0
 */


@Slf4j
@RestController
@RequestMapping("/pms/user")
@AllArgsConstructor
public class PmsUserController {

    private final PmsUserService userService;

    @GetMapping
    public JsonResult<List<PmsUser>> list() {
        return JsonResult.success(
                userService.findAll()
        );
    }

}
