package edu.cs244b.chat.gui;

import edu.cs244b.chat.ChatManager;
import edu.cs244b.chat.contracts.*;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.RoomContext;
import edu.cs244b.chat.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * UI component to display chat for the room
 *
 */
public class ChatRoom extends JPanel {

    JTextPaneExtension chatContent = null;

    public ChatRoom(RoomContext room, IGUIHandler guiHandler) {
        this.setLayout(new BorderLayout());
        //top control
        JPanel topControl = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chatContent.setText("");
                guiHandler.showRoomList();
            }
        });
        topControl.add(backButton,BorderLayout.WEST);
        Label roomName = new Label(room.getRoomName(), 1);
        roomName.setFont(new Font("Serif", Font.BOLD, 18));
        topControl.add(roomName,BorderLayout.CENTER);

        chatContent = new JTextPaneExtension();
        chatContent.setEditable(false);
        chatContent.setBackground(new Color(225, 255, 204));
        JScrollPane chatHistory = new JScrollPane(chatContent);


        JPanel input = new JPanel();
        input.setLayout(new BorderLayout());
        JTextField inputField = new JTextField();

        String IMG_PATH = "src/main/resources/send.jpg";
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(new File(IMG_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage newImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();  // get its graphics object
        g.drawImage(inputImage, 0, 0, 30, 30, null);
        g.dispose(); 
        Icon newIcon = new ImageIcon(newImage);
        JButton sendButton = new JButton(newIcon);

        //Click Event
        ActionListener sendListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String content = inputField.getText();
                if (content != null) {
                    // TODO The place where the message context is created
                    List<String> userIds = null;
                    List<String> parentMessageId = null;
                    long messageId = System.currentTimeMillis();
                    Timestamp timestamp = new Timestamp(messageId);
                    // TODO please notice the passed parameters, depth is 0, parentMessageId is empty
                    MessageContext messageContext = new MessageContext(room.getRoomId(), userIds, ChatManager.userId, parentMessageId, String.valueOf(messageId), timestamp, 0, content);
                    showMessage(messageContext, true);
                    guiHandler.saveMessage(messageContext);
                } else {
                    chatContent.append("Can't send empty content", true);
                }
                inputField.setText("");
            }
        };

        sendButton.addActionListener(sendListener);
        input.add(inputField,BorderLayout.CENTER);
        input.add(sendButton,BorderLayout.EAST);
        input.setBackground(new Color(225, 229, 204));

        topControl.setPreferredSize(new Dimension(400, 50));
        this.add(topControl,BorderLayout.NORTH);
        chatHistory.setPreferredSize(new Dimension(400, 400));
        this.add(chatHistory,BorderLayout.CENTER);
        input.setPreferredSize(new Dimension(400, 50));
        this.add(input,BorderLayout.SOUTH);
        //this.setSize(400,600);
        this.setVisible(true);
        //this.setLocation(400, 200);
    }

    public void loadMessages(List<MessageContext> messageList){
        if(messageList == null)
            return;
        messageList.forEach(msg ->{
            if(msg.getOwnerId().equals(ChatManager.userId))
                showMessage(msg, true);
            else
                showMessage(msg, false);
        });
    }

    public void showMessage(MessageContext messageContext, boolean isSelf) {
        System.out.println("----ChatRoom-----showMessage: roomId="+messageContext.getRoomId() + ", msg="+messageContext.getMessageContent());
        if (isSelf) {
            chatContent.append(messageContext.getMessageContent()+ " " + Utils.getDate(messageContext.getTimestamp())+"\n",true);
        }
        else {
            String message = "["+messageContext.getOwnerId()+"]:"+messageContext.getMessageContent();
            chatContent.append(message+" " +Utils.getDate(messageContext.getTimestamp())+"\n", false);
        }
    }

}