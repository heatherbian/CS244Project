package edu.cs244b.chat.contracts;

import edu.cs244b.chat.model.ConnectionContext;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.RoomContext;

import java.util.List;

public interface IStorageHandler {

	/**
	 * Return all local saved messages
	 *
	 */
	List<MessageContext> getAllMessages();

	/**
	 * Return all local saved messages which belong to the specific room
	 *
	 */
	List<MessageContext> getMessages(String roomId);

	/**
	 * Return a list of all rooms
	 *
	 */
	List<RoomContext> getRoomList();

	/**
	 * Return the data model of one specific room
	 *
	 */
	RoomContext getRoom(String roomId);

	/**
	 * Save the user's all messages to local storage
	 *
	 */
	void saveMessageContexts(String userId);

	/**
	 * Save the user's all messages to local storage
	 *
	 */
	void saveMessageContexts(String userId, List<MessageContext> messageContexts);

	/**
	 * Save the user's all connections to local storage
	 *
	 */
	void saveConnectionContexts(String userId);

	/**
	 * Return all connections
	 *
	 */
	List<ConnectionContext> getConnectionList();

	/**
	 * Add the message of one specific room to memory
	 *
	 */
	void addMessage(String roomId, MessageContext messageContext);
}
