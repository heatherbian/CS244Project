package edu.cs244b.chat.transportation;

import edu.cs244b.chat.contracts.*;
import edu.cs244b.chat.model.ConnectionContext;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.MessageRequest;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageMonitor implements IMessageMonitor {

    private Map<String, PeerListener> peerMap = new ConcurrentHashMap<>();

    private IStorageHandler storageHandler;
    private IMessageHandler messageHandler;
    private IMessageNotifier roomObserver;

    public MessageMonitor(IStorageHandler storageHandler, IMessageHandler messageHandler, IMessageNotifier roomObserver) {
        this.storageHandler = storageHandler;
        this.messageHandler = messageHandler;
        this.roomObserver = roomObserver;
    }

    @Override
    public void buildConnections() {
        List<ConnectionContext> connList = storageHandler.getConnectionList();
        for(ConnectionContext conn: connList) {
            buildConnection(conn);
        }
        new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch (Exception e) {}
            messageHandler.sendMessageRequest(new MessageRequest(true, true, null));
        }).start();
    }

    @Override
    public void acceptMessageContext(MessageContext messageContext) {
        messageHandler.receiveMessage(messageContext);
    }

    @Override
    public void acceptMessageRequest(MessageRequest messageRequest) {
        messageHandler.handleMessageRequest(messageRequest);
    }

    @Override
    public void removePeerListener(ConnectionContext conn) {
        if(peerMap.containsKey(conn.getUserId()))
            peerMap.remove(conn.getUserId());
        buildConnection(conn);
    }

    private void buildConnection(ConnectionContext conn) {
        new ConnectionCreator(conn, this, this.roomObserver).start();
    }

    class ConnectionCreator extends Thread {

        private ConnectionContext conn;
        private IMessageMonitor transportationHandler;
        private IMessageNotifier roomObserver;

        ConnectionCreator(ConnectionContext connectionContext, IMessageMonitor transportationHandler, IMessageNotifier roomObserver){
            this.conn = connectionContext;
            this.transportationHandler = transportationHandler;
            this.roomObserver = roomObserver;
        }

        void buildConn(ConnectionContext conn){
            Socket socket = null;
            try {
                // System.out.println("++++++++++Try to connect "+conn.getUserId()+"++++++++++++.");
                socket = new Socket(conn.getHostName(), conn.getPort());
                PeerListener peerListener = new PeerListener(conn, socket, transportationHandler, roomObserver);
                peerListener.start();
                interrupt();
            } catch (Exception e) {
                // Suppress connection error message
                // e.printStackTrace();
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                // System.out.println("=============Try to reconnect "+conn.getUserId()+"==============.");
                try {
                    Thread.sleep(3000);
                    buildConn(conn);
                } catch (InterruptedException ex) {
                    // Suppress connection error message
                    // ex.printStackTrace();
                    buildConn(conn);
                }
            }
        }

        @Override
        public void run(){
            buildConn(conn);
        }
    }

}
