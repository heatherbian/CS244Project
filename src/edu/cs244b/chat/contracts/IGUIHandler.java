package edu.cs244b.chat.contracts;

import java.util.List;

public interface IGUIHandler {

	/**
	 * Respond the UI event to show message
	 * 
	 * @param msessageContext
	 */
	void showMessage(MessageContext msessageContext);
	
	/**
	 * Respond the UI event to send the message
	 * 
	 * @param msessageContext
	 */
	void sendMessage(MessageContext msessageContext);
	
	/**
	 * Respond the UI event to show all rooms and users
	 * 
	 * @return a list of String which contains room name or user name
	 */
	List<String> showList();
	
	/**
	 * Respond the UI event to add one room or user for chat
	 * 
	 * @param destination IP address, user name or room name
	 */
	void addConnection(String destination);
}
