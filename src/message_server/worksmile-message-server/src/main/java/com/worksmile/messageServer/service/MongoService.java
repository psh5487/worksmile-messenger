package com.worksmile.messageServer.service;

import com.worksmile.messageServer.model.message.EventMessage;
import com.worksmile.messageServer.model.message.SavingMessage;
import com.worksmile.messageServer.model.collection.Messages;
import com.worksmile.messageServer.model.enums.MessageType;
import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Service
public class MongoService {

    private final MongoTemplate mongoTemplate;

    public MongoService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public SavingMessage saveMessageToMongo(EventMessage eventMessage) {
        String roomId = eventMessage.getRuuid();

        // 해당 방의 마지막 메시지의 midx 알아내기
        Long lastMidx = findLastMidx(roomId);

        if(lastMidx == -1L) { // 해당 방이 존재하지 않는 경우
            return createDocument(eventMessage.getRuuid()); // 임시 구현

        } else {
            SavingMessage savingMessage = SavingMessage.builder()
                    .type(eventMessage.getType())
                    .midx(lastMidx + 1)
                    .parent_id(eventMessage.getParent_id())
                    .content(eventMessage.getContent())
                    .sender(eventMessage.getSender())
                    .uname(eventMessage.getUname())
                    .cname(eventMessage.getCname())
                    .pname(eventMessage.getPname())
                    .device(eventMessage.getDevice())
                    .created_at(eventMessage.getCreated_at())
                    .deleted_at(eventMessage.getDeleted_at())
                    .build();

            Query query = new Query();
            query.addCriteria(
                    Criteria.where("ruuid").is(roomId)
            );

            Update update = new Update();
            update.push("msg", savingMessage);

            mongoTemplate.updateFirst(query, update, "messages");
            return savingMessage;
        }
    }

    public SavingMessage saveFirstMessageToMongo(EventMessage eventMessage) {
        SavingMessage savingMessage = SavingMessage.builder()
                .type(eventMessage.getType())
                .midx(eventMessage.getMidx())
                .parent_id(eventMessage.getParent_id())
                .content(eventMessage.getContent())
                .sender(eventMessage.getSender())
                .uname(eventMessage.getUname())
                .cname(eventMessage.getCname())
                .pname(eventMessage.getPname())
                .device(eventMessage.getDevice())
                .created_at(eventMessage.getCreated_at())
                .deleted_at(eventMessage.getDeleted_at())
                .build();

        List<SavingMessage> list = new LinkedList<>();
        list.add(savingMessage);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ruuid", eventMessage.getRuuid());
        jsonObject.put("msg", list);

        mongoTemplate.insert(jsonObject, "messages");
        return savingMessage;
    }

    public Long findLastMidx(String roomId) {
        BasicQuery query = new BasicQuery("{ruuid: " + "\"" + roomId + "\"" + "}", "{msg: {$slice: -1}}");
        Messages lastMessage = mongoTemplate.findOne(query, Messages.class, "messages");

        if(lastMessage == null) {
            return -1L;
        } else {
            return lastMessage.getMsg().get(0).getMidx();
        }
    }

    public void deleteDocument(String roomId) {
        Query query = new Query();
        query.addCriteria(
                Criteria.where("ruuid").is(roomId)
        );
        mongoTemplate.remove(query, "messages");
    }

    public SavingMessage createDocument(String roomId) {
        SavingMessage savingMessage = SavingMessage.builder()
                .type(MessageType.CREATE_ROOM)
                .midx(0L)
                .parent_id(0L)
                .content(roomId + " 방이 생성 되었습니다.")
                .sender("알림")
                .uname("")
                .cname("")
                .pname("")
                .device("system")
                .created_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .deleted_at("")
                .build();

        List<SavingMessage> list = new LinkedList<>();
        list.add(savingMessage);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ruuid", roomId);
        jsonObject.put("msg", list);

        mongoTemplate.insert(jsonObject, "messages");
        return savingMessage;
    }
}
