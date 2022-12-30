package com.xaaef.molly.system.cron;

import cn.hutool.core.util.IdUtil;
import com.xaaef.molly.common.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.xaaef.molly.common.consts.WebSocketKey.TOPIC;

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


    @Scheduled(fixedRate = 5000)
    public void cron1() {
        var map = Map.of(
                "id", IdUtil.objectId(),
                "msg", IdUtil.fastSimpleUUID()
        );
        var json = JsonUtils.toJson(map);
        log.info(json);
        messagingTemplate.convertAndSend("/topic/hello", json);
    }


}
