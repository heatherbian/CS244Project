package edu.cs244b.chat.contracts;

import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.MessageRequest;

import java.util.List;

public interface IMessageHandler {

    /**
     * Receive the message from other servers
     *
     */
    void receiveMessage(MessageContext message);

    /**
     * Handle the message generated by the UI itself
     *
     */
    void handleMessage(MessageContext message);

    /**
     * Save all message to local storage
     *
     */
    void storeData();

    /**
     * Return all messages which belong to the same room
     *
     */
    List<MessageContext> getAllMessagesFromRoom(String roomId);

    /**
     * Handle the MessageRequest sent by other servers and broadcast the messages to all servers
     *
     */
    void handleMessageRequest(MessageRequest messageRequest);

    /**
     * Send a MessageRequest to other servers.
     *
     */
    void sendMessageRequest(MessageRequest messageRequest);
}
