package edu.cs244b.chat.contracts;

public interface IMessageSender {

    /**
     * Send the message to all connected servers
     *
     */
    void sendMessage(String message);
}
