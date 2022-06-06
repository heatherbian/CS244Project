package edu.cs244b.chat.storage;

import java.util.*;
import java.util.stream.Collectors;

import edu.cs244b.chat.model.ConnectionContext;
import edu.cs244b.chat.contracts.IStorageHandler;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.RoomContext;

public class StorageHandler implements IStorageHandler {

	private Map<String, List<MessageContext>> messages;
	private List<ConnectionContext> connectionContexts;
	private List<RoomContext> rooms;
	private List<MessageContext> messageContexts;

	public StorageHandler(String userId) {
		messageContexts = FileHandler.readMessage(userId);
		if (messageContexts == null || messageContexts.isEmpty()){
			messages = new HashMap<>();
			messageContexts = new LinkedList<>();
		}
		else {
			messages = messageContexts.stream().collect(Collectors.groupingBy(MessageContext::getRoomId));
		}
		connectionContexts = FileHandler.readConnection(userId);
		rooms = FileHandler.readRoom(userId);
	}

	@Override
	public List<MessageContext> getAllMessages() {
		return messageContexts;
	}

	@Override
	public List<MessageContext> getMessages(String roomId) {
		List<MessageContext> list = messages.get(roomId);
		return list;
	}

	@Override
	public List<RoomContext> getRoomList() {
		return rooms;
	}

	@Override
	public RoomContext getRoom(String roomId) {
		return rooms.stream().filter(room -> room.getRoomId().equals(roomId)).findFirst().orElse(null);
	}

	@Override
	public void saveMessageContexts(String userId) {
		List<MessageContext> data = new LinkedList<>();
		messages.values().forEach(list -> {
			data.addAll(list);
		});
		data.sort((mc1, mc2) -> mc1.compareTo(mc2));
		if (!data.isEmpty())
			FileHandler.writeMessage(userId, data);
	}

	@Override
	public void saveMessageContexts(String userId, List<MessageContext> messageContexts) {
		if(!messageContexts.isEmpty())
			FileHandler.writeMessage(userId, messageContexts);
	}

	@Override
	public void saveConnectionContexts(String userId) {
		connectionContexts.stream().sorted();
		if (!connectionContexts.isEmpty())
			FileHandler.writeConnection(userId, connectionContexts);
	}

	@Override
	public List<ConnectionContext> getConnectionList() {
		return connectionContexts;
	}

	@Override
	public void addMessage(String roomId, MessageContext messageContext) {
		if(containsRoom(messageContext)){
			if (messages.containsKey(roomId))
				messages.get(roomId).add(messageContext);
			else {
				List<MessageContext> messageContexts = new LinkedList<>();
				messageContexts.add(messageContext);
				messages.put(roomId, messageContexts);
			}
		}
		//skip unknown room's message
	}

	private boolean containsRoom(MessageContext messageContext) {
		for (RoomContext room: rooms) {
			if(room.getRoomId().equals(messageContext.getRoomId()))
				return true;
		}
		return false;
	}

}
