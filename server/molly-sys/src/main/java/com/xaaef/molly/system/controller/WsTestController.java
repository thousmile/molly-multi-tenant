package com.xaaef.molly.system.controller;

import com.xaaef.molly.common.consts.WebSocketKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/30 14:35
 */


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/ws/test")
public class WsTestController {

    private final SimpMessagingTemplate messagingTemplate;


    /**
     * 发送广播消息
     * -- 说明：
     *       1）、@MessageMapping注解对应客户端的stomp.send('url')；
     *       2）、用法一：要么配合@SendTo("转发的订阅路径")，去掉 messagingTemplate，同时 return msg 来使用，return msg会去找@SendTo注解的路径；
     *       3）、用法二：要么设置成void，使用messagingTemplate来控制转发的订阅路径，且不能return msg，个人推荐这种。
     *
     * @param msg 消息
     */
    @MessageMapping("/send")
    public void sendAll(@RequestParam String msg) {
        log.info("[发送消息]>>>> msg: {}", msg);
        // 发送消息给客户端
        messagingTemplate.convertAndSend(WebSocketKey.TOPIC, msg);
    }


}
