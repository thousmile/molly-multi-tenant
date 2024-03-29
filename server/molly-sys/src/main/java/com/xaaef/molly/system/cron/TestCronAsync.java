package com.xaaef.molly.system.cron;

import cn.hutool.core.util.RandomUtil;
import com.xaaef.molly.auth.service.JwtTokenService;
import com.xaaef.molly.common.domain.SimpPushMessage;
import com.xaaef.molly.common.util.IdUtils;
import com.xaaef.molly.common.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.xaaef.molly.common.consts.SimpMessageConst.BROADCAST_NOTICE;
import static com.xaaef.molly.common.consts.SimpMessageConst.QUEUE_SINGLE_PUSH;

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

    private final static AtomicInteger count1 = new AtomicInteger(8760);

    @Scheduled(fixedRate = 30000)
    public void cron1() {
        if (!tokenService.listLoginIds().isEmpty()) {
            var ind = count1.decrementAndGet();
            var msg = new SimpPushMessage()
                    .setId(IdUtils.getStandaloneId())
                    .setTitle(String.format("广播消息=>%d", RandomUtil.randomInt(10000, 99999)))
                    .setContent(randomChinese(20))
                    .setCreateTime(LocalDateTime.now());
            var json = JsonUtils.toJson(msg);
            // 广播通知  给所有用户
            messagingTemplate.convertAndSend(BROADCAST_NOTICE, json);
            if (ind == 1) {
                count2.set(8760);
            }
        }
    }


    private final static AtomicInteger count2 = new AtomicInteger(8760);


    @Scheduled(fixedRate = 60000)
    public void cron2() {
        var ind = count2.decrementAndGet();
        tokenService.mapAllLoginUser().forEach((userId, user) -> {
            var msg = new SimpPushMessage()
                    .setId(IdUtils.getStandaloneId())
                    .setTitle(String.format("推送消息=>%d", RandomUtil.randomInt(10000, 99999)))
                    .setContent(randomChinese(20))
                    .setCreateTime(LocalDateTime.now());
            var json = JsonUtils.toJson(msg);
            // 推送给指定用户 ,  /user/queue/single/push
            messagingTemplate.convertAndSendToUser(user.getLoginId(), QUEUE_SINGLE_PUSH, json);
            if (ind == 1) {
                count2.set(8760);
            }
        });
    }


    public static String randomChinese(int len) {
        var chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = RandomUtil.randomChinese();
        }
        return new String(chars);
    }


}
