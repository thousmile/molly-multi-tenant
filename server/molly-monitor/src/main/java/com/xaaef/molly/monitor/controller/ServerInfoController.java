package com.xaaef.molly.monitor.controller;

import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.monitor.entity.ServerInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/server/info")
@AllArgsConstructor
@Tag(name = "[ 通用 ] 服务器")
public class ServerInfoController {


    @Operation(summary = "服务器详情", description = "获取服务器详情，内存，CPU，磁盘")
    @GetMapping()
    public JsonResult<ServerInfo> info() throws Exception {
        var serverInfo = new ServerInfo();
        serverInfo.copyTo();
        return JsonResult.success(serverInfo);
    }


    @Operation(summary = "测试hello1", description = "测试hello1")
    @PreAuthorize("hasAuthority('abc')")
    @GetMapping("hello1")
    public JsonResult<String> hello1() throws Exception {
        return JsonResult.success("hello1");
    }


    @Operation(summary = "测试hello2", description = "测试hello1")
    @PreAuthorize("hasAuthority('pre_user:view')")
    @GetMapping("hello2")
    public JsonResult<String> hello2() throws Exception {
        throw new Exception("hello2");
        //return JsonResult.success("2");
    }



    @PreAuthorize("hasAuthority('pre_user:view')")
    @GetMapping("hello3")
    public JsonResult<String> hello3() throws Exception {
        throw new Exception("hello3");
        //return JsonResult.success("2");
    }


}
