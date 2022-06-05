package edu.cs244b.chat.gui;

import edu.cs244b.chat.ChatManager;

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

/**
 * UI component to display login
 *
 */
public class LoginInterface extends JPanel{

    JFrame logIn;
    public LoginInterface() {
        logIn = new JFrame ("Login to chatting room");
        logIn.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //roomID Panel
        JPanel roomIdPanel = new JPanel();
        roomIdPanel.add(new JLabel(""));
        roomIdPanel.setBackground(new Color(229, 255, 204));
    
        //userName Panel
        JPanel userNamePanel = new JPanel();
        userNamePanel.setBackground(new Color(229,255, 204));
        JLabel userNameLabel = new JLabel("UserName");
        JTextField userNameText = new JTextField(20);
        userNamePanel.add(userNameLabel);
        userNamePanel.add(userNameText);

        // image login button
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(new Color(204, 255, 229));
        String IMG_PATH = "src/main/resources/login.jpg";
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(new File(IMG_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage newImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();  // get its graphics object
        g.drawImage(inputImage, 0, 0, 50, 50, null);
        g.dispose(); 
        Icon newIcon = new ImageIcon(newImage);
        JButton imageLoginButton = new JButton(newIcon);
        loginPanel.add(imageLoginButton);

        imageLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userId = userNameText.getText();
                if(!ChatManager.addrMap.containsKey(userId))
                    return;
                try {
                    new ChatManager(userId);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
                logIn.setVisible(false);
            }
        });

        Box box = Box.createVerticalBox();
        box.add(roomIdPanel);
        box.add(userNamePanel);
        box.add(loginPanel);
        logIn.setContentPane(box);
        logIn.pack();
        logIn.setLocationRelativeTo(null);;
        logIn.setVisible(true);
    }   
}
