package edu.cs244b.chat.transportation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageSender extends Thread{
    private PeerRegister peerRegister;
    private Socket socket;
    private PrintWriter printWriter;

    public MessageSender(Socket socket, PeerRegister peerRegister) {
        this.socket = socket;
        this.peerRegister = peerRegister;
    }

    public void run() {
        try {
            System.out.println("=====MessageSender= " + this.getName() + ", ServerRegister= "+ peerRegister.getName()+"==portNum="+socket.getPort()+"=== 1");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((this.socket.getInputStream())));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            while(true) {
                String message = bufferedReader.readLine();
                // TODO remove it later
                peerRegister.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            peerRegister.getServerThreadThreads().remove(this);
        }
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}
