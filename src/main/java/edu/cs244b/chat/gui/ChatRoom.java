package edu.cs244b.chat.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class ChatRoom extends JFrame{
    JButton sendButton;
    JTextField inputField;
    JTextArea chatContent;

    public ChatRoom() throws IOException {
        this.setLayout(new BorderLayout());
        chatContent = new JTextArea(12,34);

        //chatContent
        JScrollPane chatHistory = new JScrollPane(chatContent);

        chatContent.setEditable(false);
        chatContent.setBackground(new Color(225, 255, 204));

        JPanel input = new JPanel();
        inputField = new JTextField(38);

        //sendButton = new JButton("send");


        String IMG_PATH = "src/main/resources/send.jpg";
        BufferedImage inputImage = ImageIO.read(new File(IMG_PATH));
        BufferedImage newImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();  // get its graphics object
        g.drawImage(inputImage, 0, 0, 30, 30, null);
        g.dispose(); 
        Icon newIcon = new ImageIcon(newImage);
        JButton sendButton = new JButton(newIcon);

        //Click Event
        ActionListener sendListener = new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                String content = inputField.getText();
                
                if (content != null) {
                    // Todo: remove line 57, 
                    // 1, call send message API! send message content to backend
                    // 2, get list message API!!! append messages into chat Content, there should be message.username : message.message context
                    chatContent.append(content+"\n");
                } else {
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
        input.setBackground(new Color(225, 229, 204));

        //
        this.add(chatHistory,BorderLayout.CENTER);
        this.add(input,BorderLayout.SOUTH);
        this.setSize(600,400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocation(400, 200);    
    }

    public static void main(String[] args) throws IOException {
        LoginInterface login1 = new LoginInterface();
    }
}