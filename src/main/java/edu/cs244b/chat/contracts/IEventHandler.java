package edu.cs244b.chat.contracts;

import java.util.List;

public interface IEventHandler {

	/**
	 * Analyze the message graph of the current app itself by checking local storage and the passed messages.
	 * It is called on demand when the app is running
	 * 
	 * @param messageContext 
	 * @return a list that could contain one or multiple messages ordered for UI displaying
	 */
	List<MessageContext> analyzeMessage(List<MessageContext> messageContext);
	
	/**
	 * Sync local messages based on existing storage and the passed messages.
	 * It is called when the app starts/restarts.
	 * 
	 * @param messageContext
	 * @return a list that could contain one or multiple messages ordered for UI displaying
	 */
	List<MessageContext> syncMessage(List<MessageContext> messageContext);
}
