package edu.cs244b.chat.contracts;

import org.apache.commons.math3.util.Pair;

import java.util.List;

public class MessageRequest {
    // Whether we need to request message history from other servers. If this boolean is set to false, there is no need
    // to send requests to other servers.
    public boolean needMessagesFromOtherServers;

    // A list of requests for each room
    public List<Pair<String, Pair<Integer, Integer>>> requests;

    public MessageRequest(boolean needMessagesFromOtherServers, List<Pair<String, Pair<Integer, Integer>>> requests) {
        this.needMessagesFromOtherServers = needMessagesFromOtherServers;
        this.requests = requests;
    }
}
