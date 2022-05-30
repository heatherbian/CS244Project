package edu.cs244b.chat.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UserList extends JFrame {
    JButton join;
    JTextArea UserList;
    //JTextField what;

    public UserList() {
        this.setLayout(new BorderLayout());
        UserList = new JTextArea();
        UserList.setSize(300, 500);
        
        // loop get full user list from []
        UserList.setText("Name");
        
        JScrollPane UserListPanel = new JScrollPane(UserList);

        UserList.setEditable(false);

        /*JLabel List = new JLabel("A");
        UserList.add(List);*/

        JPanel buttonPanel = new JPanel();
        /*what = new JTextField();*/

        join = new JButton("Join The Room");
        buttonPanel.add(join);
        /*buttonPanel.add(what);*/

        JPanel NamePanel = new JPanel();
        JLabel NameLable = new JLabel("Your Name");
        NamePanel.add(NameLable);

        //join room

        join.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click join room button!");
                try {
                    ChatRoom room = new ChatRoom();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                UserList.setVisible(false);
            }
        });


        this.add(UserListPanel,BorderLayout.CENTER);
        this.add(NamePanel,BorderLayout.NORTH);
        this.add(buttonPanel,BorderLayout.SOUTH);
        this.setSize(300,400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocation(400, 200);
    }
    
    
}
