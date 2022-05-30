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
        userNamePanel.setBackground(new Color(229,255, 204));
        JLabel userNameLabel = new JLabel("UserName");
        JTextField userNameText = new JTextField(20);
        userNamePanel.add(userNameLabel);
        userNamePanel.add(userNameText);
    
        //roomID Panel
        JPanel roomIdPanel = new JPanel();
        roomIdPanel.add(new JLabel("RoomId"));
        roomIdPanel.setBackground(new Color(229, 255, 204));
        roomIdPanel.add(new JTextField (20));

        // image login button
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(new Color(204, 255, 229));
        String IMG_PATH = "src/main/resources/login.jpg";
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
