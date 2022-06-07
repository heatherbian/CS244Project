package edu.cs244b.chat.contracts;

import edu.cs244b.chat.model.ConnectionContext;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.MessageRequest;

public interface IMessageMonitor {
	
	/**
	 * Build the peer connection
	 *
	 */
	void buildConnections();

	/**
	 * Accept the MessageContext from peer
	 *
	 */
	void  acceptMessageContext(MessageContext messageContext);

	/**
	 * Accept the MessageRequest from peer
	 *
	 */
	void acceptMessageRequest(MessageRequest messageRequest);

	/**
	 * Remove the saved PeerListener and will build new peer connection
	 *
	 */
	void removePeerListener(ConnectionContext connectionContext);
}
