package edu.cs244b.chat.gui;

import edu.cs244b.chat.ChatManager;
import edu.cs244b.chat.contracts.IGUIHandler;
import edu.cs244b.chat.contracts.IMessageHandler;
import edu.cs244b.chat.model.MessageRequest;
import edu.cs244b.chat.model.RoomContext;
import edu.cs244b.chat.model.RoomListModel;
import edu.cs244b.chat.transportation.MessageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * UI component to display the room list
 *
 */
public class RoomList extends JPanel {

    public RoomList(IGUIHandler handler, RoomListModel roomListModel, IMessageHandler messageHandler) {

        this.messageHandler = messageHandler;

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        Label info = new Label(ChatManager.userId);
        info.setForeground(new Color(16,125,174,255));
        info.setFont(new Font("Serif", Font.BOLD, 20));
        topPanel.add(info);
        topPanel.setBackground(new Color(229,255, 204));;

        JList roomList = new JList(roomListModel);
        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setFont(new Font("Serif", Font.BOLD, 18));
        roomList.setFocusable(false);
        roomList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    int index = roomList.getSelectedIndex();
                    String roomName = (String)roomList.getModel().getElementAt(index);
                    RoomContext room =  handler.getRoom(roomName);
                    handler.showRoom(room);
                    System.out.println("enter: "+roomList.getModel().getElementAt(index)+" room");
                }

            }
        });
        roomList.setBackground(new Color(204, 255, 229));
        DefaultListCellRenderer renderer =  (DefaultListCellRenderer)roomList.getCellRenderer();  
        renderer.setHorizontalAlignment(JLabel.CENTER);  
        roomList.setBounds(0,0,400, 550);

        JPanel bottomPanel = new JPanel();
        String statusStr = ChatManager.networkOn ? "Connected":"Disconnected";
        JButton networkButton = new JButton("Network " + statusStr);
        networkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChatManager.networkOn = !ChatManager.networkOn;
                String statusStr = ChatManager.networkOn ? "Connected":"Disconnected";
                networkButton.setText("Network " + statusStr);
                System.out.println("Current network status is: " + ChatManager.networkOn);
                if (ChatManager.networkOn) {
                    messageHandler.sendAllMessages();
                    messageHandler.sendMessageRequest(new MessageRequest(true,true,null));
                }
            }
        });
        bottomPanel.setBackground(new Color(229,255, 204));;
        bottomPanel.add(networkButton);

        add(topPanel, BorderLayout.NORTH);
        add(roomList, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private IMessageHandler messageHandler;
}
