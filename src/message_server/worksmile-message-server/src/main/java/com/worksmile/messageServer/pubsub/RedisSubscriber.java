package com.worksmile.messageServer.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worksmile.messageServer.model.message.EventMessage;
import com.worksmile.messageServer.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RedisSubscriber  {

    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisRepository redisRepository;

    public RedisSubscriber(SimpMessageSendingOperations messagingTemplate, RedisRepository redisRepository) {
        this.messagingTemplate = messagingTemplate;
        this.redisRepository = redisRepository;
    }

    /**
     * 대기하고 있던 Redis Subscriber 가 Publish 된 메시지 처리
     * */
    public void sendMessage(String publishedMessage) {
        try {
            // ChatMessage 객체로 맵핑
            ObjectMapper objectMapper = new ObjectMapper();
            EventMessage message = objectMapper.readValue(publishedMessage, EventMessage.class);
            log.info("RedisSubscriber : {}", message);

            String roomId = message.getRuuid();

            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/msg/room/" + roomId, message);

            // 해당 채팅방에 속해 있는 모든 Connected 사용자에게 메시지 발송 (채팅방리스트 실시간 반영을 위함)
            Set<String> connectedUsersOfRoom = redisRepository.getConnectedUsersOfRoom(roomId);

            for(String userId : connectedUsersOfRoom) {
                messagingTemplate.convertAndSend("/sub/msg/user/" + userId, message);
            }
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}
