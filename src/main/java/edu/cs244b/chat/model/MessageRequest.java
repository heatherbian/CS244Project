package edu.cs244b.chat.model;

import org.apache.commons.math3.util.Pair;

import java.util.List;

public class MessageRequest {
    // Whether we need to request message history from other servers. If this boolean is set to false, there is no need
    // to send requests to other servers.
    public boolean needMessagesFromOtherServers;

    // Whether return everything that the server has to the other server.
    public boolean syncEverything;

    // A list of requests for each room
    public List<Pair<String, Pair<Integer, Integer>>> requests;

    public MessageRequest(boolean needMessagesFromOtherServers, boolean syncEverything, List<Pair<String, Pair<Integer, Integer>>> requests) {
        this.needMessagesFromOtherServers = needMessagesFromOtherServers;
        this.syncEverything = syncEverything;
        this.requests = requests;
    }
}
