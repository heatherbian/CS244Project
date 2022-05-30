package edu.cs244b.chat.storage;

import java.util.*;
import java.util.stream.Collectors;

import edu.cs244b.chat.contracts.ConnectionContext;
import edu.cs244b.chat.contracts.IStorageHandler;
import edu.cs244b.chat.contracts.MessageContext;
import edu.cs244b.chat.utils.FileHandler;

public class StorageHandler implements IStorageHandler {

	Map<String, List<MessageContext>> messages;
	Map<String, List<ConnectionContext>> connections;

	public StorageHandler(String userId) {
		List<MessageContext> messageContexts = FileHandler.readMessage(userId);
		if (messageContexts == null || messageContexts.isEmpty())
			messages = new HashMap<>();
		else {
			messages = messageContexts.stream().collect(Collectors.groupingBy(MessageContext::getRoomId));
		}
		List<ConnectionContext> connectionContexts = FileHandler.readConnection(userId);
		if (connectionContexts == null || connectionContexts.isEmpty())
			connections = new HashMap<>();
		else
			connections = connectionContexts.stream().collect(Collectors.groupingBy(ConnectionContext::getRoomId));
	}

//	@Override
//	public MessageContext getMessage(String messageId) {
//		//List<MessageContext> list = FileHandler.readMessage();
//		MessageContext messageContext = list.stream().filter(e -> e.getMessageId().equals(messageId)).findAny().orElse(null);
//		return messageContext;
//	}

	@Override
	public MessageContext getMessage(String roomId, long messageId) {
		List<MessageContext> list = messages.get(roomId);
		if (list == null || list.isEmpty())
		return null;
		return list.stream().filter(e -> e.getMessageId().equals(messageId)).findAny().orElse(null);
	}

	@Override
	public List<MessageContext> getMessages(String roomId) {
		List<MessageContext> list = messages.get(roomId);
		return list;
	}

	@Override
	public String createMessage(MessageContext messageContext) {
		//FileHandler.writeMessage(messageContext.getOwnerId(), messageContext);
		return null;
	}

	@Override
	public boolean updateMessage(MessageContext msessageContext) {
		// TODO Auto-generated method stub
		//MessageContext messageContext = getMessage(msessageContext.getMessageId());
		return false;
	}

	@Override
	public boolean deleteMessage(MessageContext msessageContext) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<MessageContext> getRoomMessage(String roomId, long messageId) {
		List<MessageContext> list = messages.get(roomId);
		//return list.stream().filter(e -> e.getMessageId().equals(messageId)).collect(Collectors.toList());
		List<MessageContext> res = new LinkedList<>();
		int i = 0;
		for(; i < list.size(); i++)
			if(list.get(i).getMessageId().equals(messageId))
				break;
		res.addAll(list.subList(i+ 1, list.size()));
		return res;
	}

	@Override
	public List<MessageContext> getUserMessage(String userId, long messageId) {
		return getRoomMessage(userId, messageId);
	}

	@Override
	public List<String> getRoomList() {
		return connections.keySet().stream().toList();
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
	public void saveConnectionContexts(String userId) {
		List<ConnectionContext> data = new LinkedList<>();
		connections.values().forEach(list ->{
			data.addAll(list);
		});
		data.stream().sorted();
		if (!data.isEmpty())
			FileHandler.writeConnection(userId, data);
	}

	@Override
	public void saveConnectionList(String userId, List<ConnectionContext> list) {
		FileHandler.writeConnection(userId, list);
	}

	@Override
	public List<ConnectionContext> getConnectionList(String roomId) {
		return connections.get(roomId);
	}

	@Override
	public void updateConnection(String roomId, ConnectionContext connectionContext) {
		if (connections.containsKey(roomId))
			connections.get(roomId).add(connectionContext);
		else {
			List<ConnectionContext> connectionContexts = new LinkedList<>();
			connectionContexts.add(connectionContext);
			connections.put(roomId, connectionContexts);
		}
	}

	@Override
	public void updateConnections(String roomId, List<ConnectionContext> connectionContexts) {
		connections.put(roomId, connectionContexts);
	}

	@Override
	public void updateMessage(String roomId, MessageContext messageContext) {
		if (messages.containsKey(roomId))
			messages.get(roomId).add(messageContext);
		else {
			List<MessageContext> messageContexts = new LinkedList<>();
			messageContexts.add(messageContext);
			messages.put(roomId, messageContexts);
		}
	}


}
