package com.xaaef.molly.web.ws;

import com.xaaef.molly.auth.enums.OAuth2Error;
import com.xaaef.molly.auth.exception.JwtAuthException;
import com.xaaef.molly.auth.service.JwtTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/30 14:05
 */


@Slf4j
@Component
@AllArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenService jwtTokenService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // 1、判断是否首次连接
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            var props = jwtTokenService.getProps();
            // 2、获取 token
            var bearerToken = accessor.getFirstNativeHeader(props.getTokenHeader());
            if (StringUtils.isNotBlank(bearerToken)) {
                try {
                    var token = jwtTokenService.validate(bearerToken);
                    // 如果存在用户信息，将用户名赋值，后期发送时，可以指定 loginId 即可发送到对应用户
                    accessor.setUser(token);
                } catch (JwtAuthException e) {
                    log.error("WebSocket 认证失败！{}", e.getMessage());
                    return new ErrorMessage(e);
                }
            } else {
                return new ErrorMessage(new JwtAuthException(OAuth2Error.ACCESS_TOKEN_INVALID));
            }
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }


}
