package com.xaaef.molly.monitor.controller;

import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.monitor.entity.ServerInfo;
import com.xaaef.molly.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/server/info")
@AllArgsConstructor
@Tag(name = "[ 通用 ] 服务器")
public class ServerInfoController {

    public final SysConfigService baseService;


    @Operation(summary = "服务器详情", description = "获取服务器详情，内存，CPU，磁盘")
    @GetMapping()
    public JsonResult<ServerInfo> info() throws Exception {
        var serverInfo = new ServerInfo();
        serverInfo.copyTo();
        return JsonResult.success(serverInfo);
    }


}
