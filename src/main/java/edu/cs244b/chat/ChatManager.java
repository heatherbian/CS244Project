package edu.cs244b.chat;

import edu.cs244b.chat.contracts.*;
import edu.cs244b.chat.transportation.MessageHandler;
import edu.cs244b.chat.gui.LoginInterface;
import edu.cs244b.chat.gui.ViewController;
import edu.cs244b.chat.storage.StorageHandler;
import edu.cs244b.chat.transportation.PeerRegister;
import edu.cs244b.chat.transportation.MessageMonitor;

import java.io.IOException;
import java.util.Map;

/**
 * The main thread which is running all time once it starts
 *
 */
public class ChatManager {

	public static String userId;
	public static String portNum;
	public static boolean networkOn;
	public static Map<String, String> addrMap = Map.of("Shaohui", "10001", "Henry", "10002", "Jingyi", "10003");

	public static void main(String[] args) throws Exception{
		networkOn = true;
		LoginInterface p1=new LoginInterface();
		p1.setVisible(true);
	}

	public ChatManager(String userName) throws IOException {
		userId = userName;
		portNum = addrMap.get(userId);
		IStorageHandler storageHandler = new StorageHandler(userId);
		PeerRegister peerRegister = new PeerRegister(ChatManager.portNum);
		peerRegister.start();

		IMessageHandler messageHandler = new MessageHandler(storageHandler, peerRegister);

		ViewController frame = new ViewController(storageHandler, messageHandler);

		IMessageMonitor messageMonitor = new MessageMonitor(storageHandler, messageHandler, frame);
		messageMonitor.buildConnections();

		frame.setVisible(true);
		frame.showRoomList();
	}
}