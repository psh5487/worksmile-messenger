package com.worksmile.messageServer.service;

import com.worksmile.messageServer.model.enums.MessageType;
import com.worksmile.messageServer.model.message.EventMessage;
import com.worksmile.messageServer.model.message.SavingMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
@SpringBootTest
class MongoServiceTest {
    @Autowired
    MongoService mongoService;

    @Test
    public void saveMessageToMongoTest() {
        EventMessage eventMessage = EventMessage.builder()
                .ruuid("bb")
                .type(MessageType.TALK)
                .parent_id(0L)
                .content("안녕하세요")
                .sender("shparkc")
                .uname("박소현")
                .cname("Stove")
                .pname("인턴")
                .device("web")
                .created_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                .deleted_at("")
                .build();

        SavingMessage message = mongoService.saveMessageToMongo(eventMessage);
        System.out.println(message);
    }

    @Test
    public void findLastMidxTest() {
        Long lastMidx = mongoService.findLastMidx("jj");
        if(lastMidx == null)
            System.out.println("null");
    }

    @Test
    public void createDocumentTest() {
        EventMessage eventMessage = EventMessage.builder()
                .ruuid("jj")
                .type(MessageType.CREATE_ROOM)
                .midx(1L)
                .parent_id(0L)
                .content("누구누구 멤버로 방이 생성되었습니다.")
                .sender("알림")
                .uname("")
                .cname("")
                .pname("")
                .device("system")
                .created_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                .deleted_at("")
                .build();

        mongoService.createDocument(eventMessage.getRuuid());
    }
}