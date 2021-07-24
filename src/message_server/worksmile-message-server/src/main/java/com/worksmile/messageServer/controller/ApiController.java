package com.worksmile.messageServer.controller;

import com.worksmile.messageServer.model.dto.ApiResult;
import com.worksmile.messageServer.model.dto.CreateRoomEventRequest;
import com.worksmile.messageServer.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class ApiController {

    private final MessageService messageService;

    public ApiController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/test/message-server")
    public String hello() {
        return "Hello!";
    }

    @PostMapping("/create/room")
    public ApiResult sendCreateRoomEvent(@RequestBody CreateRoomEventRequest request) {
        try {
            messageService.publishCreateRoomEventMessage(request);
            Map<String, String> map = new HashMap<>();
            map.put("ruuid", request.getRuuid());
            return new ApiResult(200, "채팅방 생성 이벤트 처리 성공", map);
        } catch (RuntimeException e) {
            return new ApiResult(400, e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete/room/{ruuid}")
    public ApiResult sendDeleteRoomEvent(@PathVariable String ruuid) {
        try {
            messageService.publishDeleteRoomEventMessage(ruuid);
            Map<String, String> map = new HashMap<>();
            map.put("ruuid", ruuid);
            return new ApiResult(200, "채팅방 소멸 이벤트 처리 성공", map);
        } catch (RuntimeException e) {
            return new ApiResult(400, e.getMessage(), null);
        }
    }
}
