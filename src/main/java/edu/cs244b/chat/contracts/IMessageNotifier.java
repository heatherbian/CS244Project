package edu.cs244b.chat.contracts;

import edu.cs244b.chat.model.MessageContext;

public interface IMessageNotifier {

    /**
     * Notify the UI component that there is one coming message from other servers
     *
     */
    void notifyNewMessage(MessageContext messageContext);
}
