package com.worksmile.messageServer.model.collection;

import com.worksmile.messageServer.model.message.SavingMessage;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "messages")
@Data
public class Messages {
    @Id
    private Object _id;
    private String ruuid;
    private List<SavingMessage> msg;
}
