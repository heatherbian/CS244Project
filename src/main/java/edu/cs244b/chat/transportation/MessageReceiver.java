package edu.cs244b.chat.transportation;

import com.google.gson.Gson;
import edu.cs244b.chat.contracts.ConnectionContext;
import edu.cs244b.chat.contracts.IStorageHandler;
import edu.cs244b.chat.contracts.MessageContext;

import javax.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver extends Thread{
    private ConnectionContext connectionContext;
    private BufferedReader bufferedReader;
    private IStorageHandler storageHandler;
    public MessageReceiver(ConnectionContext connectionContext, Socket socket, IStorageHandler storageHandler) throws IOException {
        this.connectionContext = connectionContext;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.storageHandler = storageHandler;
    }
    public void run() {
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("!!!!!Peer= " + this.getName() + "!!!!! 1");
                JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
                Gson gson = new Gson();
                MessageContext messageContext = gson.fromJson(jsonObject.toString(), MessageContext.class);
                System.out.println("["+messageContext.getOwnerId()+"]:"+messageContext.getMessageContent());
                storageHandler.updateMessage(connectionContext.getRoomId(), messageContext);
            } catch (Exception e) {
                flag = false;
                interrupt();
            }
        }
    }
}
