package edu.cs244b.chat.transportation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageSender extends Thread{
    private ServerRegister serverRegister;
    private Socket socket;
    private PrintWriter printWriter;

    public MessageSender(Socket socket, ServerRegister serverRegister) {
        this.socket = socket;
        this.serverRegister = serverRegister;
    }

    public void run() {
        try {
            System.out.println("=====MessageSender= " + this.getName() + ", ServerRegister= "+ serverRegister.getName()+"===== 1");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((this.socket.getInputStream())));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            while(true) {
                String message = bufferedReader.readLine();
                // TODO remove it later
                serverRegister.sendMessage(message);
            }
        } catch (Exception e) {
            serverRegister.getServerThreadThreads().remove(this);
        }
    }


    public PrintWriter getPrintWriter() {
        return printWriter;
    }


}
