package edu.cs244b.chat.transportation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import edu.cs244b.chat.contracts.*;
import edu.cs244b.chat.model.ConnectionContext;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.MessageRequest;

import javax.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PeerListener extends Thread{
    private ConnectionContext connectionContext;
    private BufferedReader bufferedReader;
    private IMessageMonitor transportationHandler;
    private IMessageNotifier roomObserver;
    public PeerListener(ConnectionContext connectionContext, Socket socket, IMessageMonitor transportationHandler, IMessageNotifier roomObserver) throws IOException {
        this.connectionContext = connectionContext;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.transportationHandler = transportationHandler;
        this.roomObserver = roomObserver;
    }
    public void run() {
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("!!!!!Peer= " + this.getName() + "!!!!! 1  connUserId="+connectionContext.getUserId());
                JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
                Gson gson = new Gson();
                try {
                    System.out.println("+++++MessageReceiver connUserId="+connectionContext.getUserId()+", receive msg=["+jsonObject.toString()+"]");
                    MessageContext messageContext = gson.fromJson(jsonObject.toString(), MessageContext.class);
                    transportationHandler.acceptMessageContext(messageContext);
                    roomObserver.notifyNewMessage(messageContext);
                } catch (JsonSyntaxException jsonExp) {
                    System.out.println("--------------JsonSyntaxException in MessageReceiver------Try MessageRequest-----------");
                    MessageRequest messageRequest = gson.fromJson(jsonObject.toString(), MessageRequest.class);
                    transportationHandler.acceptMessageRequest(messageRequest);
                }
            } catch (Exception e) {
                System.out.println("--------------Exception in MessageReceiver-----------------");
                e.printStackTrace();
                transportationHandler.removePeerListener(connectionContext);
                flag = false;
                interrupt();
            }
        }
    }
}
