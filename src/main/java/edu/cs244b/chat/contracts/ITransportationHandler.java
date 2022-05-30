package edu.cs244b.chat.contracts;

import java.util.List;

public interface ITransportationHandler {

	
	/**
	 * broadcast the message to room or specific user
	 * 
	 * @param messageContext
	 */
	void broadcastMessage(MessageContext messageContext);
	
	/**
	 * Build the socket connection
	 * 
	 * @param destination
	 */
	void buildConnection(String destination);
	
	/**
	 * Accept received message
	 * 
	 * @return
	 */
	List<MessageContext> receiveMessage();
}
