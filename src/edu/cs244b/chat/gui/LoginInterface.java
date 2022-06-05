package edu.cs244b.chat.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class LoginInterface {

    public LoginInterface() throws IOException {
        JFrame logIn = new JFrame ("Login to chatting room");
        logIn.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
    
        //userName Panel
        JPanel userNamePanel = new JPanel();
        userNamePanel.setBackground(new Color(255,255, 255));
        JLabel userNameLabel = new JLabel("UserName");
        userNameLabel.setForeground(new Color(16,125,174,255));
        JTextField userNameText = new JTextField(20);
        userNamePanel.add(userNameLabel);
        userNamePanel.add(userNameText);
    
        //roomID Panel
        JPanel roomIdPanel = new JPanel();
        JLabel roomIdLabel = new JLabel("RoomId   ");
        roomIdLabel.setForeground(new Color(16,125,174,255));
        roomIdPanel.add(roomIdLabel);
        roomIdPanel.setBackground(new Color(255, 255, 255));
        roomIdPanel.add(new JTextField (20));

        // image login button
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(new Color(255, 255, 255));
        String IMG_PATH = "/Users/Joanne/Downloads/CS244Project/src/edu/cs244b/chat/gui/login.jpg";        
        BufferedImage inputImage = ImageIO.read(new File(IMG_PATH));
        BufferedImage newImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();  // get its graphics object
        g.drawImage(inputImage, 0, 0, 100, 100, null);
        g.dispose(); 
        Icon newIcon = new ImageIcon(newImage);
        JButton imageLoginButton = new JButton(newIcon);
        loginPanel.add(imageLoginButton);

        //Login event
        /*class ClickListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                // Todo: call api, login method, POST user id and room id!!!!
                System.out.println("Clicked!");
            }
         }

        //Add click event for the login button
        loginButton.addActionListener(new ClickListener());
        loginButton.doClick();*/
        imageLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked!");
                try {
                    UserList userList = new UserList();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                logIn.setVisible(false);
            }
        });
        
        userNamePanel.setSize(300, 30);
        roomIdPanel.setSize(300, 30);
        loginPanel.setSize(300, 100);
        logIn.add(userNamePanel, BorderLayout.NORTH);
        logIn.add(roomIdPanel, BorderLayout.CENTER);
        logIn.add(loginPanel, BorderLayout.SOUTH);
        logIn.setSize(400,230);
        logIn.setLocationRelativeTo(null);; 
        logIn.setVisible(true);
    }   
}
