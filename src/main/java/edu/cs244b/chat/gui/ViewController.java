package edu.cs244b.chat.gui;

import edu.cs244b.chat.contracts.*;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.RoomContext;
import edu.cs244b.chat.model.RoomListModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * UI component to manage ChatRoom and RoomList, and support logic control
 *
 */
public class ViewController extends JFrame implements IGUIHandler, IMessageNotifier {

    private CardLayout cardLayout = new CardLayout();
    private JPanel panel = new JPanel();
    private Map<String, ChatRoom> roomMap = new HashMap<>();
    IStorageHandler storageHandler;
    IMessageHandler messageHandler;
    private static String currentRoomName;

    /**
     * Create the frame.
     */
    public ViewController(IStorageHandler storageHandler, IMessageHandler messageHandler) {
        setTitle("P2P Chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new ViewWindowListener());
        setBounds(100, 100, 400, 600);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        panel.setBounds(0, 0, 400, 570);
        contentPane.add(panel);
        panel.setLayout(cardLayout);
        cardLayout.show(panel, "p1");
        this.storageHandler = storageHandler;
        this.messageHandler = messageHandler;
    }

    @Override
    public void showRoomList() {

        RoomList p2 = new RoomList(this, new RoomListModel(storageHandler.getRoomList()), messageHandler);
        panel.add(p2, "p2");
        cardLayout.show(panel, "p2");
    }

    @Override
    public void showRoom(RoomContext room) {
        ChatRoom chatRoom = null;
        if (roomMap.containsKey(room.getRoomId())) {
            chatRoom = roomMap.get(room.getRoomId());
        } else {
            System.out.println("$$$$$   " + this.getName() + " +showRoom: create=" + room.getRoomName());
            chatRoom = new ChatRoom(room, this);
            panel.add(chatRoom, room.getRoomId());
            roomMap.put(room.getRoomId(), chatRoom);
        }
        currentRoomName = room.getRoomName();
        chatRoom.loadMessages(messageHandler.getAllMessagesFromRoom(room.getRoomId()));
        cardLayout.show(panel, room.getRoomId());
    }

    @Override
    public void saveMessage(MessageContext message) {
        messageHandler.handleMessage(message);
    }

    @Override
    public RoomContext getRoom(String roomName) {
        for (RoomContext obj : storageHandler.getRoomList()) {
            if (obj.getRoomName().equals(roomName)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public void notifyNewMessage(MessageContext messageContext) {
        ChatRoom chatRoom = null;
        RoomContext room = storageHandler.getRoom(messageContext.getRoomId());
        if (room == null)
            return;
        if (roomMap.containsKey(messageContext.getRoomId())) {
            chatRoom = roomMap.get(room.getRoomId());
        } else {
            System.out.println("$$$$$" + this.getName() + "+showRoom: create=" + room.getRoomName());
            chatRoom = new ChatRoom(room, this);
            panel.add(chatRoom, room.getRoomId());
            roomMap.put(room.getRoomId(), chatRoom);
        }
        if (room.getRoomName().equals(currentRoomName))
            chatRoom.loadMessages(messageHandler.getAllMessagesFromRoom(room.getRoomId()));
    }

    class ViewWindowListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent e) {}
        @Override
        public void windowClosing(WindowEvent e) {
            messageHandler.storeData();
        }
        @Override
        public void windowClosed(WindowEvent e) {}
        @Override
        public void windowIconified(WindowEvent e) {}
        @Override
        public void windowDeiconified(WindowEvent e) {}
        @Override
        public void windowActivated(WindowEvent e) {}
        @Override
        public void windowDeactivated(WindowEvent e) {}
    }
}
