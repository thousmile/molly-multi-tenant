package com.xaaef.molly.common.consts;

/**
 * <p>
 * 全局推送消息
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2023/11/08 15:15
 */


public class SimpMessageConst {

    // 广播 通知
    public static final String BROADCAST_NOTICE = "/topic/broadcast/notice";

    // 指定 推送
    public static final String QUEUE_SINGLE_PUSH = "/queue/single/push";

    // 租户创建成功或者失败推送
    public static final String QUEUE_SINGLE_CREATE_TENANT = "/queue/single/create/tenant";

}
