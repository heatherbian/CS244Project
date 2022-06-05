package edu.cs244b.chat.transportation;

import edu.cs244b.chat.contracts.IMessageSender;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class PeerRegister extends Thread implements IMessageSender {

    private ServerSocket serverSocket;
    private Set<MessageSender> messageSenders = new HashSet<MessageSender>();
    private String portNumber;

    public PeerRegister(String portNumber) throws IOException {
        serverSocket = new ServerSocket(Integer.valueOf(portNumber));
        this.portNumber = portNumber;
    }

    public void run(){
        try {
            while (true) {
                System.out.println("-----ServerRegister= " + this.getName() + "--"+portNumber+"--- 1");
                MessageSender messageSender = new MessageSender(serverSocket.accept(), this);
                messageSenders.add(messageSender);
                messageSender.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String message) {
        try {
            messageSenders.forEach(t -> {
                System.out.println("ServerRegister: " + t.getName() + " is sending message={"+message+"}");
                t.getPrintWriter().println(message);});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Set<MessageSender> getServerThreadThreads() {
        return messageSenders;
    }
}
