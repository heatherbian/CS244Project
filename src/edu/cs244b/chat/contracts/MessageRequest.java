package edu.cs244b.chat.contracts;

public class MessageRequest {
    public boolean needMessagesFromOtherServers;  // Whether we need to request message history from other servers.
    public int startDepth;  // The lower bound depth of message we need.
    public int endDepth; // The upper bound depth of the messages we need. If set to -1, it means that there is no upper bound.

    public MessageRequest(boolean needMessagesFromOtherServers, int startDepth, int endDepth) {
        this.needMessagesFromOtherServers = needMessagesFromOtherServers;
        this.startDepth = startDepth;
        this.endDepth = endDepth;
    }
}
