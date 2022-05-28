package edu.cs244b.chat.contracts;

import java.util.List;

public interface IStorageHandler {

	MessageContext getMessage(String messageId);

	/**
	 * @param msessageContext
	 * @return the message id if create successfully, otherwise null
	 */
	String createMessage(MessageContext msessageContext);

	boolean updateMessage(MessageContext msessageContext);

	boolean deleteMessage(MessageContext msessageContext);

	/**
	 * @param roomId
	 * @return all messages belong to the room ordered by
	 *         {@link MessageContext#messageId}. If messageId it not empty, return
	 *         messages after the specific message.
	 */
	List<MessageContext> getRoomMessage(String roomId, String messageId);

	/**
	 * @param roomId
	 * @return all messages belong to the chat with the user ordered by
	 *         {@link MessageContext#messageId}. If messageId it not empty, return
	 *         messages after the specific message.
	 */
	List<MessageContext> getUserMessage(String userId, String messageId);

	/**
	 * @return a list of String which contains room name or user name
	 */
	List<String> getList();
}
