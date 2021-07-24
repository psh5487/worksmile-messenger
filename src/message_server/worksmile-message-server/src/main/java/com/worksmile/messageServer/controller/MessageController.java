package com.worksmile.messageServer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.worksmile.messageServer.model.message.EventMessage;
import com.worksmile.messageServer.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * websocket 으로 들어오는 메시지 발행 처리
     * 클라이언트 요청 - /pub/msg
     */
    @MessageMapping("/msg")
    public void message(@Payload EventMessage message) throws ParseException, JsonProcessingException {
        messageService.publishMessage(message);
    }
}
