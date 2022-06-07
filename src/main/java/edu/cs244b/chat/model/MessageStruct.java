package edu.cs244b.chat.model;

import java.util.List;

public class MessageStruct {
    public boolean isMessageContext;
    public List<MessageContext> messageContexts;
    public MessageRequest messageRequest;

    public MessageStruct(List<MessageContext> msgs) {
        messageContexts = msgs;
        isMessageContext = true;
    }

    public MessageStruct(MessageRequest req) {
        isMessageContext = false;
        messageRequest = req;
    }
}
