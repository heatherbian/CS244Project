package edu.cs244b.chat.contracts;

import edu.cs244b.chat.model.MessageContext;

import java.util.List;

public interface IMessagePublisher {

    /**
     * Broadcast the message to all connected servers
     *
     */
    void broadcastMessages(List<MessageContext> messageContexts);
}
