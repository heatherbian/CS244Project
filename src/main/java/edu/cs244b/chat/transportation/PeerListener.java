package edu.cs244b.chat.transportation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import edu.cs244b.chat.contracts.*;
import edu.cs244b.chat.model.ConnectionContext;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.MessageRequest;
import edu.cs244b.chat.model.MessageStruct;

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

                System.out.println("+++++MessageReceiver connUserId="+connectionContext.getUserId()+", receive msg=["+jsonObject.toString()+"]");
                MessageStruct msg = gson.fromJson(jsonObject.toString(), MessageStruct.class);
                if(msg.isMessageContext) {
                    for (MessageContext messageContext : msg.messageContexts) {
                        transportationHandler.acceptMessageContext(messageContext);
                        roomObserver.notifyNewMessage(messageContext);
                    }
                } else {
                    transportationHandler.acceptMessageRequest(msg.messageRequest);
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
