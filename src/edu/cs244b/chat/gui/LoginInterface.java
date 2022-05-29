package edu.cs244b.chat.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginInterface {
    
    public LoginInterface() {
        JFrame logIn = new JFrame ("login to chatting room");
        logIn.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
        //userName Panel
        JPanel userNamePanel = new JPanel();
        JLabel userNameLabel = new JLabel("userName");
        JTextField userNameText = new JTextField(20);

        userNamePanel.add(userNameLabel);
        userNamePanel.add(userNameText);
    
        //roomID Panel
        JPanel roomIdPanel = new JPanel();
        roomIdPanel.add(new JLabel("roomId"));
        roomIdPanel.add(new JTextField (10));

        //login Panel
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("login");
        loginPanel.add(loginButton);

        //Login event
        class ClickListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                // Todo: call api, login method, POST user id and room id
                System.out.println("Clicked!");
            }
         }

        //Add click event for the login button
        loginButton.addActionListener(new ClickListener());
        loginButton.doClick();


        //VerticalBox
        Box box = Box.createVerticalBox();
        box.add(userNamePanel);
        box.add(roomIdPanel);
        box.add(loginPanel);

        logIn.setContentPane(box);
        logIn.pack();
        logIn.setLocationRelativeTo(null);;
        logIn.setVisible(true);
    }




    
}
