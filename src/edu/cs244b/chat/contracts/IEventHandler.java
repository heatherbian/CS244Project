package edu.cs244b.chat.contracts;

import sun.plugin2.message.Message;

import java.util.List;

public interface IEventHandler {

	/**
	 * Analyze the messages and add them to the EventGraph as appropriate.
	 * It is called when we receive messages from other servers or when the server just started (pass in messages from
	 * storage).
	 *
	 * @param messageContext 
	 * @return a message request that indicates if further messages is required from other servers.
	 */
	MessageRequest analyzeMessage(List<MessageContext> messageContext, String userId);
	
	/**
	 * Store the new message to the event graph and generate the List of messageContext for broadcasting to other
	 * servers.
	 * It is called when the local client sends a new message in the GUI.
	 * 
	 * @param messageContext It is required that messageContext poassed in has the following fields populated:
	 * timestamp, ownerId, messageContent, roomId.
	 * @return a list of messageContext that should be broadcast to other servers.
	 */
	List<MessageContext> handleNewMessage(MessageContext messageContext);

	/**
	 * Handle message history requests from other servers. Returns the requested message history in the form a list of messageContext.
	 * It is called when the another server sends us a messageRequest.
	 *
	 * @param messageRequest
	 * @return a list of messageContext that should be broadcast to other servers.
	 */
	List<MessageContext> handleMessageRequest(MessageRequest messageRequest);


	/**
	 * Get all messages attached to the event graph for a room.
	 * Called when the main thread needs info for displaying the messages.
	 *
	 * @param roomId
	 * @return a list that could contain one or multiple messages from the event graph.
	 */
	List<MessageContext> getAllMessagesFromRoom(String roomId);

	/**
	 * Get all messages attached to the event graphs in **all rooms**.
	 * Called when the main thread needs to store messages into the local storage.
	 *
	 * @param
	 * @return a list that could contain one or multiple messages from the event graph.
	 */
	List<MessageContext> getAllMessages();
}
