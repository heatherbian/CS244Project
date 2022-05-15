package edu.cs244b.chat.storage;

import java.util.List;

import edu.cs244b.chat.contracts.IStorageHandler;
import edu.cs244b.chat.contracts.MessageContext;

public class StorageHandler implements IStorageHandler {

	@Override
	public MessageContext getMessage(String messageId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createMessage(MessageContext msessageContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateMessage(MessageContext msessageContext) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteMessage(MessageContext msessageContext) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<MessageContext> getRoomMessage(String roomId, String messageId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageContext> getUserMessage(String userId, String messageId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getList() {
		// TODO Auto-generated method stub
		return null;
	}

}
