package com.worksmile.messageServer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worksmile.messageServer.model.dto.CreateRoomEventRequest;
import com.worksmile.messageServer.model.enums.MessageType;
import com.worksmile.messageServer.pubsub.RedisPublisher;
import com.worksmile.messageServer.model.message.EventMessage;
import com.worksmile.messageServer.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageService {

    private final RedisPublisher redisPublisher;
    private final RedisRepository redisRepository;
    private final MongoService mongoService;

    public MessageService(RedisPublisher redisPublisher, RedisRepository redisRepository, MongoService mongoService) {
        this.redisPublisher = redisPublisher;
        this.redisRepository = redisRepository;
        this.mongoService = mongoService;
    }

    /**
     * Redis 에 메시지 Publish
     */
    public void publishMessage(EventMessage message) throws ParseException, JsonProcessingException {
        String roomId = message.getRuuid();
        String userId = message.getSender();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (MessageType.ENTER.equals(message.getType())) {
            String contentJsonStr = message.getContent(); // users 리스트 추출

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<String>> map = objectMapper.readValue(contentJsonStr, Map.class);
            List<String> users = map.get("users");

            message.setContent(userId + "가 " + users.toString() + "을 초대하였습니다.");
            message.setSender("알림");
            message.setCreated_at(timestamp);

            Long newMidx = mongoService.saveMessageToMongo(message).getMidx();
            message.setMidx(newMidx);

            redisPublisher.publish(message);

        } else if (MessageType.EXIT.equals(message.getType())) {
            message.setContent(userId + "님이 나갔습니다.");
            message.setSender("알림");
            message.setCreated_at(timestamp);

            Long newMidx = mongoService.saveMessageToMongo(message).getMidx();
            message.setMidx(newMidx);

            redisPublisher.publish(message);

        } else if(MessageType.ON.equals(message.getType())) {
            redisRepository.addOnUserToRoom(roomId, userId);

            // 결과 content 만들기
            Set<String> onUsersOfRoom = redisRepository.getOnUsersOfRoom(roomId);
            Integer onUserCnt = onUsersOfRoom.size();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("on_user_list", new ArrayList<>(onUsersOfRoom));
            jsonObject.put("on_user_cnt", onUserCnt);

            message.setContent(jsonObject.toJSONString());
            message.setCreated_at(timestamp);
            redisPublisher.publish(message);

        } else if(MessageType.OFF.equals(message.getType())) {
            redisRepository.removeOnUserFromRoom(roomId, userId);

            // last_read_idx 값 추출
            String content = message.getContent();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Long> map = objectMapper.readValue(content, Map.class);
            Long lastReadIdx = ((Number) map.get("last_read_idx")).longValue();

            // 결과 content 만들기
            Set<String> onUserSet = redisRepository.getOnUsersOfRoom(roomId);
            Integer onUserCnt = onUserSet.size();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("last_read_idx", lastReadIdx);
            jsonObject.put("on_user_list", new ArrayList<>(onUserSet));
            jsonObject.put("on_user_num", onUserCnt);

            message.setContent(jsonObject.toJSONString());
            message.setCreated_at(timestamp);
            redisPublisher.publish(message);

        } else {
            message.setCreated_at(timestamp);

            Long newMidx = mongoService.saveMessageToMongo(message).getMidx();
            message.setMidx(newMidx);

            redisPublisher.publish(message);
        }
    }

    /** 채팅방 생성 이벤트 메시지 보내기 */
    public void publishCreateRoomEventMessage(CreateRoomEventRequest request) {
        String roomId = request.getRuuid();

        // 중복 확인
        if(mongoService.findLastMidx(roomId) != -1) {
            throw new RuntimeException("이미 존재하는 방입니다.");
        }

        // MongoDB에 저장
        EventMessage message = EventMessage.builder()
                .type(MessageType.CREATE_ROOM)
                .midx(0L)
                .parent_id(0L)
                .ruuid(request.getRuuid())
                .content(request.getRname() + " 방이 생성되었습니다. " + "멤버 : " + request.getUsers().toString())
                .sender("알림")
                .uname("")
                .cname("")
                .pname("")
                .device("system")
                .created_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .deleted_at("")
                .build();
        mongoService.saveFirstMessageToMongo(message);

        // Connected Users 관리
        List<String> users = request.getUsers();
        List<String> connectedUsers = users.stream()
                .filter(userId -> redisRepository.getConnectedUser(userId) != null)
                .collect(Collectors.toList());

        String[] connectedUsersArr = new String[connectedUsers.size()];
        redisRepository.addMultipleConnectedUsersToRoom(roomId, connectedUsers.toArray(connectedUsersArr));

        // 메시지 전송
        redisPublisher.publish(message);
    }

    /** 채팅방 삭제 이벤트 메시지 보내기 */
    public void publishDeleteRoomEventMessage(String roomId) {
        EventMessage message = EventMessage.builder()
                .type(MessageType.DELETE_ROOM)
                .midx(0L)
                .parent_id(0L)
                .ruuid(roomId)
                .content(roomId + " 방이 삭제되었습니다.")
                .sender("알림")
                .uname("")
                .cname("")
                .pname("")
                .device("system")
                .created_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .deleted_at("")
                .build();

        mongoService.deleteDocument(roomId);
        redisPublisher.publish(message);
    }
}
