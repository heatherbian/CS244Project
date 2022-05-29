package edu.cs244b.chat.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatRoom extends JFrame{
    JButton sendButton;
    JTextField inputField;
    JTextArea chatContent;

    public ChatRoom() {
        this.setLayout(new BorderLayout());
        chatContent = new JTextArea(12,34);

        //chatContent
        JScrollPane chatHistory = new JScrollPane(chatContent);

        chatContent.setEditable(false);
        JPanel input = new JPanel();
        inputField = new JTextField(20);

        sendButton = new JButton("send");

        //Click Event
        ActionListener sendListener = new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                String content = inputField.getText();
                
                if (content != null) {
                    // Todo: send message API
                    chatContent.append(content+"\n");
                }else {
                    chatContent.append("Can't send empty content");
                }
                inputField.setText("");
            }
        };

        sendButton.addActionListener(sendListener);

        //
        Label label = new Label("Content");
        input.add(label);
        input.add(inputField);
        input.add(sendButton);

        //
        this.add(chatHistory,BorderLayout.CENTER);
        this.add(input,BorderLayout.SOUTH);
        this.setSize(400,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    
    
    }

    public static void main(String[] args) {
        ChatRoom room1 = new ChatRoom();
        //LoginInterface login1 = new LoginInterface();
    }

}

