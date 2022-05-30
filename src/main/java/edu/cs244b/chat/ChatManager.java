package edu.cs244b.chat;

import com.google.gson.Gson;
import edu.cs244b.chat.contracts.ConnectionContext;
import edu.cs244b.chat.contracts.IStorageHandler;
import edu.cs244b.chat.contracts.MessageContext;
import edu.cs244b.chat.storage.StorageHandler;
import edu.cs244b.chat.transportation.MessageReceiver;
import edu.cs244b.chat.transportation.ServerRegister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main thread which is running all time once it starts
 *
 */
public class ChatManager {

	public static String userId;
	public static String portNum;
	public static IStorageHandler storageHandler;

	public static void main(String[] args) throws Exception{

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("> enter username & port # for your client:");
		String[] setupValues = bufferedReader.readLine().split(" ");

		userId = setupValues[0];
		portNum = setupValues[1];

		storageHandler = new StorageHandler(userId);
		ServerRegister serverRegister = new ServerRegister(portNum);
		serverRegister.start();

		new ChatManager().updateListenToPeers(bufferedReader, serverRegister);

	}

	public void updateListenToPeers(BufferedReader bufferedReader, ServerRegister serverRegister) throws Exception {
		System.out.println("ChatManager= " + this.getClass() + ", server= "+ serverRegister.getName() + ", userId="+userId+", portNum="+portNum);
		List<String> roomIds = storageHandler.getRoomList();
		String roomId = null;
		if (roomIds == null || roomIds.isEmpty()) {
			System.out.println("> enter room name");
			String roomName = bufferedReader.readLine();
			System.out.println("> enter (space separated) hostname:port#");
			//System.out.println(" peers to receive messages from (s to skip)");
			String input = bufferedReader.readLine();
			String[] inputValues = input.split(" ");
			if (!input.equals("s")) {
				List<ConnectionContext> list = new LinkedList<>();
				for (int i = 0; i < inputValues.length; i++) {
					String[] value = inputValues[i].split(":");
					ConnectionContext connectionContext = new ConnectionContext(roomName, value[0], Integer.valueOf(value[1]));
					list.add(connectionContext);
					buildPeerThread(connectionContext);
				}
				// calculate the hash key for the unified room id
				ConnectionContext self = new ConnectionContext(roomName, "127.0.0.1", Integer.valueOf(portNum));
				list.add(self);
				list.sort((cc1, cc2) -> cc1.compareTo(cc2));
				StringBuilder addrFusion = new StringBuilder();
				list.forEach(connectionContext -> {
					System.out.print(connectionContext.getHostName() + connectionContext.getPort()+"->");
					addrFusion.append(connectionContext.getHostName() + connectionContext.getPort());
				});
				final String roomID = String.valueOf(addrFusion.toString().hashCode());
				list.remove(self);
				list.stream().forEach(connectionContext -> {
					connectionContext.setRoomId(roomID);
				});
				roomId = roomID;
				storageHandler.updateConnections(roomId, list);
			}
		} else {
			roomIds.forEach(id -> {
						List<ConnectionContext> connectionContexts = storageHandler.getConnectionList(id);
						connectionContexts.forEach(connectionContext -> {
							try {
								buildPeerThread(connectionContext);
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
					}
			);
			// for multiple room use later
			//System.out.println("> enter room name");
			//String roomName = bufferedReader.readLine();
			// just use the first roomId
			roomId = roomIds.get(0);
		}

		communicate(bufferedReader, serverRegister, roomId);
	}

	public void communicate(BufferedReader bufferedReader, ServerRegister serverRegister, String roomId) {

		List<ConnectionContext> connectionContexts = storageHandler.getConnectionList(roomId);
		// the users are not really used now
		List<String> users = connectionContexts.stream().map(connectionContext -> {
			return connectionContext.getRoomId();
		}).collect(Collectors.toList());
		try{
			System.out.println("> you can now communicate (e to exit, c to change)");
			boolean flag = true;
			while (flag) {
				String message = bufferedReader.readLine();
				if (message.equals("e")){
					flag = false;
					break;
				} else if(message.equals("c")) {
					updateListenToPeers(bufferedReader, serverRegister);
				} else {
					List<String> parentMessageId = null;
					long messageId = System.currentTimeMillis();
					Timestamp timestamp = new Timestamp(messageId);
					MessageContext messageContext = new MessageContext(roomId, users, userId, parentMessageId, String.valueOf(messageId), timestamp, 0, message);
					Gson gson = new Gson();
					serverRegister.sendMessage(gson.toJson(messageContext));
					storageHandler.updateMessage(roomId, messageContext);
				}
			}
			storageHandler.saveConnectionContexts(userId);
			storageHandler.saveMessageContexts(userId);
			System.exit(0);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void buildPeerThread(ConnectionContext connectionContext) throws IOException {
		Socket socket = null;
		try{
			socket = new Socket(connectionContext.getHostName(), connectionContext.getPort());
			new MessageReceiver(connectionContext, socket, storageHandler).start();
		} catch (Exception e) {
			if (socket != null)
				socket.close();
			else {
				System.out.println("invalid input. skipping to next step.");
				System.out.println(e);
			}
		}
	}
}