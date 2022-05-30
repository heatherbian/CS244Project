package edu.cs244b.chat.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Dimension;


public class UserList extends JFrame {
    JButton imageLoginButton;
    JTextArea UserList;

    public UserList() throws IOException {
        JFrame j = new JFrame();
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
        UserList = new JTextArea();
        UserList.setBackground(new Color(204, 229, 255));
        
        // User list panel
        Font font1 = new Font("Tahoma", Font.BOLD, 15);
        // Todo: call backend api and get all users name in here 
        String[] names = {"Jingyi", "Shaohui", "Henry"}; 
        for (String name : names) {           
            UserList.setFont(font1);
            UserList.append(name + "\n");            
        }
        UserList.setEditable(false);    
        JScrollPane UserListPanel = new JScrollPane(UserList);
 
        // Joint room Panel
        JPanel buttonPanel = new JPanel();
        String IMG_PATH = "/Users/Joanne/Downloads/CS244Project/src/edu/cs244b/chat/gui/jointheroom.jpg";        
        BufferedImage inputImage = ImageIO.read(new File(IMG_PATH));
        BufferedImage newImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics(); 
        g.drawImage(inputImage, 0, 0, 64, 64, null);
        g.dispose(); 
        Icon newIcon = new ImageIcon(newImage);
        JButton imageLoginButton = new JButton(newIcon);
        buttonPanel.add(imageLoginButton);
        buttonPanel.setBackground(new Color(204, 204, 255));

        // Title panel
        JPanel NamePanel = new JPanel();
        JLabel NameLable = new JLabel("Room members");
        Font font2 = new Font("Tahoma", Font.BOLD, 18);
        NameLable.setFont(font2);
        NamePanel.add(NameLable);

        // Action: join room
        imageLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click join room button!");
              
                try {
                    ChatRoom room = new ChatRoom();
                    
                } catch (IOException e1) {
                    e1.printStackTrace();
                }               
                j.setVisible(false);
            } 
        });

        // VerticalBox
        Box box = Box.createVerticalBox();

        NamePanel.setSize(300, 20);
        UserListPanel.setSize(300, 100);
        buttonPanel.setSize(300, 30);

        // NamePanel.setPreferredSize(new Dimension(300, 20));
        // UserListPanel.setPreferredSize(new Dimension(300, 20));
        // buttonPanel.setPreferredSize(new Dimension(300, 20));

        box.add(NamePanel);
        box.add(UserListPanel);
        box.add(buttonPanel);
        box.add(Box.createVerticalGlue());
        j.setContentPane(box);
        j.add(NamePanel);
        j.add(UserListPanel);
        j.add(buttonPanel);
        j.pack();
        j.setSize(400,300);
        j.setVisible(true);
        j.setLocation(400, 200);
    }  
}
