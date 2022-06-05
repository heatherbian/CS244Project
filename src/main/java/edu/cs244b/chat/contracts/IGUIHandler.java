package edu.cs244b.chat.contracts;

import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.RoomContext;

public interface IGUIHandler {

	/**
	 * Respond the UI event to show all rooms and users
	 *
	 */
	void showRoomList();

	/**
	 * Display the specific room
	 *
	 */
	void showRoom(RoomContext room);

	/**
	 * Save the message entered by UI to storage
	 *
	 */
	void saveMessage(MessageContext message);

	/**
	 * Return the room's data model
	 *
	 */
	RoomContext getRoom(String roomName);

}
