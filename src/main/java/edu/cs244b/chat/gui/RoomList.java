package edu.cs244b.chat.gui;

import edu.cs244b.chat.ChatManager;
import edu.cs244b.chat.contracts.IGUIHandler;
import edu.cs244b.chat.model.RoomContext;
import edu.cs244b.chat.model.RoomListModel;

import javax.swing.*;
import java.awt.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * UI component to display the room list
 *
 */
public class RoomList extends JPanel {

    public RoomList(IGUIHandler handler, RoomListModel roomListModel) {

        JList roomList = new JList(roomListModel);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        Label info = new Label(ChatManager.userId);
        info.setFont(new Font("Serif", Font.BOLD, 18));
        topPanel.add(info);

        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setFont(new Font("Serif", Font.BOLD, 16));
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
        roomList.setBounds(0,0,400, 550);
        add(topPanel, BorderLayout.NORTH);
        add(roomList, BorderLayout.CENTER);
    }  
}
