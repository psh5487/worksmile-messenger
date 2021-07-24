package com.worksmile.messageServer.config.handler;

import com.worksmile.messageServer.service.JwtTokenProvider;
import com.worksmile.messageServer.service.UserConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserConnectionService userConnectionService;

    /**
     * websocket 을 통해 들어온 요청이 처리되기 전 실행
     * */
    @Override
    @Nullable
    public Message preSend(Message message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info(String.valueOf(accessor.getCommand()));

//        ObjectMapper objectMapper = new ObjectMapper();
//        EventMessage eventMessage = objectMapper.readValue((byte[]) message.getPayload(), EventMessage.class);

        if (StompCommand.CONNECT == accessor.getCommand()) { // Websocket 연결 요청
            String accessToken = accessor.getFirstNativeHeader("X-Auth-Token");
            // jwtTokenProvider.validateToken(accessToken);

            String userId = accessor.getFirstNativeHeader("uid");
            userConnectionService.manageConnectedUser(userId);

        } else if(StompCommand.DISCONNECT == accessor.getCommand()) {
            String userId = accessor.getFirstNativeHeader("uid");

            if(userId != null) {
                userConnectionService.manageDisconnectedUser(userId);
            }
        }
        return message;
    }
}
