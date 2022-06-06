package edu.cs244b.chat.transportation;

import com.google.gson.Gson;
import edu.cs244b.chat.ChatManager;
import edu.cs244b.chat.contracts.*;
import edu.cs244b.chat.event.EventHandler;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.MessageRequest;
import edu.cs244b.chat.model.RoomContext;

import java.util.*;

public class MessageHandler implements IMessageHandler, IMessagePublisher{

    // TODO control whether use EventHandler or not
    private boolean disableEventHandler = false;


    private IStorageHandler storageHandler;
    private IMessageSender messageSender;
    private IEventHandler eventHandler;

    public MessageHandler(IStorageHandler storageHandler, IMessageSender messageSender){
        this.storageHandler = storageHandler;
        this.messageSender = messageSender;
        List<RoomContext> rooms = this.storageHandler.getRoomList();
        Map<String, List<String>> roomIdToUserIdList = new HashMap<>();
        for (RoomContext room: rooms) {
            roomIdToUserIdList.put(room.getRoomId(), room.getUserIds());
        }


        eventHandler = new EventHandler(roomIdToUserIdList);
        eventHandler.analyzeMessage(storageHandler.getAllMessages(), "storage");
    }

    @Override
    public void receiveMessage(MessageContext message) {
        if (disableEventHandler) {
            storageHandler.addMessage(message.getRoomId(), message);
            return;
        }

        MessageRequest request = eventHandler.analyzeMessage((Arrays.asList(message)), message.getOwnerId());
        if (!request.needMessagesFromOtherServers)
            return;
        Gson gson = new Gson();
        messageSender.sendMessage(gson.toJson(request));
        // Save messages to storage.
        List<MessageContext> allMsgs = eventHandler.getAllMessages();
        storageHandler.saveMessageContexts(ChatManager.userId, allMsgs);
    }

    @Override
    public void handleMessage(MessageContext message) {
        if (disableEventHandler) {
            storageHandler.addMessage(message.getRoomId(), message);
            broadcastMessages(Arrays.asList(message));
            return;
        }

        List<MessageContext> msgs = eventHandler.handleNewMessage(message);
        broadcastMessages(msgs);
        // Save messages to storage.
        List<MessageContext> allMsgs = eventHandler.getAllMessages();
        storageHandler.saveMessageContexts(ChatManager.userId, allMsgs);
    }

    @Override
    public void storeData() {
        if (disableEventHandler) {
            storageHandler.saveConnectionContexts(ChatManager.userId);
            storageHandler.saveMessageContexts(ChatManager.userId);
            return;
        }

        storageHandler.saveConnectionContexts(ChatManager.userId);
        List<MessageContext> allMsgs = eventHandler.getAllMessages();
        storageHandler.saveMessageContexts(ChatManager.userId, allMsgs);
    }

    @Override
    public List<MessageContext> getAllMessagesFromRoom(String roomId) {
        if (disableEventHandler) {
            return storageHandler.getMessages(roomId);
        }

        return eventHandler.getAllMessagesFromRoom(roomId);
    }

    @Override
    public void handleMessageRequest(MessageRequest messageRequest) {
        List<MessageContext> msgs = eventHandler.handleMessageRequest(messageRequest);
        broadcastMessages(msgs);
    }

    @Override
    public void broadcastMessages(List<MessageContext> messageContexts) {
        Gson gson = new Gson();
        for(MessageContext messageContext: messageContexts)
            messageSender.sendMessage(gson.toJson(messageContext));
    }

    @Override
    public void sendMessageRequest(MessageRequest messageRequest) {
        if (!messageRequest.needMessagesFromOtherServers)
            return;
        Gson gson = new Gson();
        System.out.println("Sending message request on start");
        messageSender.sendMessage(gson.toJson(messageRequest));
    }
}
