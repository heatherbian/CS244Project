package edu.cs244b.chat.transportation;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerRegister extends Thread{

    private ServerSocket serverSocket;
    private Set<MessageSender> messageSenders = new HashSet<MessageSender>();

    public ServerRegister(String portNumber) throws IOException {
        serverSocket = new ServerSocket(Integer.valueOf(portNumber));
    }

    public void run(){
        try {
            while (true) {
                System.out.println("-----ServerRegister= " + this.getName() + "----- 1");
                MessageSender messageSender = new MessageSender(serverSocket.accept(), this);
                messageSenders.add(messageSender);
                messageSender.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            messageSenders.forEach(t -> {
                System.out.println("serverThread: " + t.getName() + " is sending message={"+message+"}");
                t.getPrintWriter().println(message);});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Set<MessageSender> getServerThreadThreads() {
        return messageSenders;
    }
}
