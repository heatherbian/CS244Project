package edu.cs244b.chat.contracts;

import java.util.List;

public class MessageContext {

	String roomId;
	/**
	 * the ids of persons whom the app owner is chatting with
	 */
	List<String> userIds;
	
	/**
	 * the id of person who is signing the app
	 */
	String ownerId;
	
	/**
	 * ids of messages that are prior to the message
	 */
	List<String> parentMessageId;
	
	/**
	 * the id of the specific message. For the time being, we could use Timestamp as for comparison
	 */
	String messageId;
	
	/**
	 * the actual content of the specific message
	 */
	String messageContent;

	public MessageContext(String roomId, List<String> userIds, String ownerId, List<String> parentMessageId, String messageId,
			String messageContent) {
		super();
		this.roomId = roomId;
		this.userIds = userIds;
		this.ownerId = ownerId;
		this.parentMessageId = parentMessageId;
		this.messageId = messageId;
		this.messageContent = messageContent;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public List<String> getParentMessageId() {
		return parentMessageId;
	}

	public void setParentMessageId(List<String> parentMessageId) {
		this.parentMessageId = parentMessageId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
}
