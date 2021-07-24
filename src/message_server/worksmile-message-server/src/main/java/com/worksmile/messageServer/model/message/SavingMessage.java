package com.worksmile.messageServer.model.message;

import com.worksmile.messageServer.model.enums.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SavingMessage {
    private MessageType type;
    private Long midx;
    private Long parent_id;
    private String content;
    private String sender;
    private String uname;
    private String cname;
    private String pname;
    private String device;
    private String created_at;
    private String deleted_at;
}
