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


public class UserList extends JFrame {
    JButton imageLoginButton;
    JTextArea UserList;
    //JTextField what;

    public UserList() throws IOException {
        this.setLayout(new BorderLayout());
        UserList = new JTextArea();
        UserList.setSize(300, 500);
        UserList.setBackground(new Color(204, 229, 255));
        
        Font font1 = new Font("Tahoma", Font.BOLD, 15);
        // Todo: call backend api and get all users name in here 
        String[] names = {"Jingyi", "Shaohui", "Henry"}; 
        for (String name : names) {           
            UserList.setFont(font1);
            UserList.append(name + "\n");            
        }
        
        JScrollPane UserListPanel = new JScrollPane(UserList);

        UserList.setEditable(false);

        JPanel buttonPanel = new JPanel();

        String IMG_PATH = "/Users/Joanne/Downloads/CS244Project/src/edu/cs244b/chat/gui/jointheroom.jpg";        
        BufferedImage inputImage = ImageIO.read(new File(IMG_PATH));
        BufferedImage newImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();  // get its graphics object
        g.drawImage(inputImage, 0, 0, 64, 64, null);
        g.dispose(); 
        Icon newIcon = new ImageIcon(newImage);
        JButton imageLoginButton = new JButton(newIcon);
        buttonPanel.add(imageLoginButton);
        buttonPanel.setBackground(new Color(204, 204, 255));

        JPanel NamePanel = new JPanel();


        JLabel NameLable = new JLabel("Room members");
        Font font2 = new Font("Tahoma", Font.BOLD, 18);
        NameLable.setFont(font2);
        NamePanel.add(NameLable);

        //join room

        imageLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click join room button!");
              
                try {
                    ChatRoom room = new ChatRoom();
                } catch (IOException e1) {
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
