package com.xaaef.molly.system.cron;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import com.xaaef.molly.core.auth.service.JwtTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/30 15:19
 */


@Slf4j
@Component
@AllArgsConstructor
public class TestCronAsync {


    private final SimpMessagingTemplate messagingTemplate;


    private final JwtTokenService tokenService;


    private final static List<String> arrs = List.of(
            "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z");


    @Scheduled(fixedRate = 5000)
    public void cron1() {
        if (!tokenService.listLoginIds().isEmpty()) {
            var map = Map.of(
                    "id", RandomUtil.randomInt(10000, 99999),
                    "msg", RandomUtil.randomString(12),
                    "dataArr", RandomUtil.randomEleSet(arrs, 3)
            );
            var json = JsonUtils.toJson(map);
            log.info(json);
            // 广播通知  给所有用户
            messagingTemplate.convertAndSend("/topic/broadcast/notice", json);
        }
    }


    @Scheduled(fixedRate = 8000)
    public void cron2() {
        tokenService.listLoginUsers().forEach(user -> {
            var map = Map.of(
                    "id", RandomUtil.randomInt(10000, 99999),
                    "msg", StrUtil.format("hello : {} => {}", user.getUsername(), RandomUtil.randomString(15)),
                    "dataArr", RandomUtil.randomEleSet(arrs, 3)
            );
            var json = JsonUtils.toJson(map);
            log.info(json);
            // 推送给指定用户 ,  /user/queue/single/push
            messagingTemplate.convertAndSendToUser(user.getLoginId(), "/queue/single/push", json);
        });
    }


}
