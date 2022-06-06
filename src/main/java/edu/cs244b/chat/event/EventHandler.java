package edu.cs244b.chat.event;

import java.sql.Timestamp;
import java.util.*;

import edu.cs244b.chat.contracts.IEventHandler;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.MessageRequest;

public class EventHandler implements IEventHandler {
	// 	Deprecated constructor.
	//	public EventHandler(List<MessageContext> messages) {
	//		rooms = new HashMap<>();
	//		HashMap<String, List<MessageContext>> messagesByRoomId = SortMessagesByRoomId(messages);
	//		for (String roomId: messagesByRoomId.keySet()) {
	//			List<MessageContext> msgs = messagesByRoomId.get(roomId);
	//			Room room = new Room();
	//			room.roomId = roomId;
	//			room.eventGraph = new EventGraph(roomId);
	//			room.userIds = msgs.size() != 0 ? new ArrayList<>(msgs.get(0).getUserIds()) : new ArrayList<>();
	//			AddMessagesToEventGraph(room.eventGraph, messages);
	//		}
	//		needMessageSyc = true;
	//	}

	// Implements the IEventHandler interface. This object is instantiated when the program starts. The roomIdToUserIdList
	// provides the list of roomIds mapped to the rooms' userIds.
	public EventHandler(Map<String, List<String>> roomIdToUserIdList) {
		rooms = new HashMap<>();
		for (String roomId: roomIdToUserIdList.keySet()) {
			Room room = new Room();
			room.roomId = roomId;
			room.eventGraph = new EventGraph(roomId);
			room.userIds = new ArrayList<>(roomIdToUserIdList.get(roomId));

			rooms.put(roomId, room);
		}
		needMessageSyc = true;
	}


	@Override
	public MessageRequest analyzeMessage(List<MessageContext> messageContext, String userId) {
		System.out.println("Event handler received messages. Number of msgs: " + messageContext.size() + ".");
		for (MessageContext msg : messageContext) {
			System.out.println(msg);
		}
		// Divide incoming messages by room id
		HashMap<String, List<MessageContext>> messagesByRoomId = new HashMap<>();
		for (MessageContext message : messageContext) {
			String roomId = message.getRoomId();
			if (!messagesByRoomId.containsKey(roomId)) {
				messagesByRoomId.put(roomId, new ArrayList<>());
			}
			List<MessageContext> messages = messagesByRoomId.get(roomId);
			messages.add(message);
		}
		for (String roomId : messagesByRoomId.keySet()) {
			if (!rooms.containsKey(roomId)) {
				System.out.println("Warning!!! Unknown roomId in the input analyzeMessage");
				continue;
			}
			AddMessagesToEventGraph(rooms.get(roomId).eventGraph, messagesByRoomId.get(roomId));
		}

		// Generate message request
		if(needMessageSyc) {
			// The instance has just started.
			needMessageSyc = false;
			return new MessageRequest(true, true,null);
		}
		return new MessageRequest(false, false,null);
	}

	@Override
	public List<MessageContext> handleNewMessage(MessageContext messageContext) {
		// TODO Auto-generated method stub
		System.out.println("Handling new message:" + messageContext.getOwnerId() + ":" + messageContext.getMessageContent());
		List<MessageContext> messagesToSend = new ArrayList<>();
		messagesToSend.addAll(HandleNewMessageInternal(messageContext));
		return messagesToSend;
	}

	@Override
	public List<MessageContext> handleMessageRequest(MessageRequest messageRequest) {
		System.out.println("Received Message Request");
		return getAllMessages();
	}

	@Override
	public List<MessageContext> getAllMessages() {
		LinkedList<MessageContext> messageContexts = new LinkedList<>();
		for (String roomId : rooms.keySet()) {
			Room room = rooms.get(roomId);
			for (Event event: room.eventGraph.events.values()) {
				if(event.eventId.contains("root_event")){
					continue;
				}
				messageContexts.add(new MessageContext(roomId, new ArrayList<>(room.userIds), event.senderId, new ArrayList<>(event.parentEventIds),
						event.eventId, event.timestamp, event.depth, event.content));
			}
		}
		Collections.sort(messageContexts);
		return messageContexts;
	}

	@Override
	public List<MessageContext> getAllMessagesFromRoom(String roomId) {
		ArrayList<MessageContext> messageContexts = new ArrayList<>();
		Room room = rooms.get(roomId);
		System.out.println(room.eventGraph.events.size());
		for (Event event: room.eventGraph.events.values()) {
			if(event.eventId.contains("root_event")){
				continue;
			}
			System.out.println(room.userIds.toString());
			System.out.println(event.parentEventIds.toString());
			messageContexts.add(new MessageContext(roomId, new ArrayList<>(room.userIds), event.senderId, new ArrayList<>(event.parentEventIds),
					event.eventId, event.timestamp, event.depth, event.content));
		}
		Collections.sort(messageContexts);
		return messageContexts;
	}

	// The new_message argument is supposed to have the timestamp, owner_id, message content and the room id.
	public List<MessageContext> HandleNewMessageInternal(MessageContext new_message) {
		Room room = rooms.get(new_message.getRoomId());
		Event event = new Event();
		event.timestamp = new_message.getTimestamp();
		event.senderId = new_message.getOwnerId();
		event.content = new_message.getMessageContent();
		event.depth = room.eventGraph.maxDepth + 1;
		event.parentEventIds = room.eventGraph.eventIdsByDepth.get(room.eventGraph.maxDepth);
		event.eventId = GenerateEventId(event.timestamp, event.senderId, event.content, event.depth);
		room.eventGraph.AddEventToGraph(event);
		MessageContext message_to_send = new MessageContext(room.roomId, room.userIds, event.senderId, new ArrayList<>(event.parentEventIds),
				event.eventId, event.timestamp, event.depth, event.content);
		return Arrays.asList(message_to_send);
	}

    public HashMap<String, Room> rooms;

	private void AddMessagesToEventGraph(EventGraph eventGraph, List<MessageContext> messages) {
		System.out.println(messages);
		HashMap<String, Event> newEvents = MessagesToEvents(messages);
		List<Event> eventsSortedByDepth = new ArrayList<>(newEvents.values());
		eventsSortedByDepth.sort(Comparator.comparingInt(e -> e.depth));

		for (Event event: eventsSortedByDepth) {
			if (eventGraph.events.containsKey(event.eventId)) {
				continue;
			}
			// Check if all parents of the message already exist in the event graph.
			boolean allParentsExist = true;
			for (String parentId : event.parentEventIds) {
				if(!eventGraph.events.containsKey(parentId)) {
					allParentsExist = false;
					eventGraph.unknownEventIds.add(parentId);
				}
			}
			if (allParentsExist) {
				eventGraph.AddEventToGraph(event);
			} else {
				if(!eventGraph.pendingEvents.containsKey(event.eventId)){
					eventGraph.pendingEvents.put(event.eventId, event);
				}
			}
		}
	}

	private HashMap<String, Event> MessagesToEvents(List<MessageContext> messages) {
		HashMap<String, Event> events = new HashMap<>();
		for (MessageContext message : messages) {
			Event event = new Event(message);
			events.put(event.eventId, event);
		}
		return events;
	}

	private boolean needMessageSyc;

	// -----------------------------------------------------------------------------------------------------------------
	// Start DataStructures Only for internal use of this the EventHandler.
	public class Room {
		public String roomId;
		public ArrayList<String> userIds;
		public EventGraph eventGraph;
	}

	public class EventGraph {
		public HashMap<String, Event> events; // event_id to event.
		public HashMap<Integer, HashSet<String>> eventIdsByDepth;
		public int maxDepth; // depth of the latest events in the event graph.

		public HashSet<String> unknownEventIds;

		public  HashMap<String, Event> pendingEvents;

		// It is presumed that the event is a valid event to be appended to the graph, i.e., the parents of the event
		// are already incorporated into the graph.
		public void AddEventToGraph(Event newEvent) {
			events.put(newEvent.eventId, newEvent);
			int depth = newEvent.depth;
			if(!eventIdsByDepth.containsKey(depth)) {
				eventIdsByDepth.put(depth, new HashSet<>());
			}
			eventIdsByDepth.get(depth).add(newEvent.eventId);
			newEvent.parentEventIds = eventIdsByDepth.get(depth - 1);
			if (depth < maxDepth) {
				for(String nextLevelEventId : eventIdsByDepth.get(depth + 1)) {
					events.get(nextLevelEventId).parentEventIds.add(newEvent.eventId);
				}
			}
			maxDepth = Math.max(depth, maxDepth);
		}

		public EventGraph(String roomId) {
			events = new HashMap<>();
			eventIdsByDepth = new HashMap<>();
			maxDepth = 0;
			unknownEventIds = new HashSet<>();
			pendingEvents = new HashMap<>();

			// Add the root event to the event graph.
			Event root_event = new Event();
			root_event.eventId = roomId + "root_event";
			root_event.depth = 0;
			root_event.senderId = "root";
			root_event.parentEventIds = new HashSet<>();
			root_event.timestamp = new Timestamp(0); // unix epoch
			root_event.content = "root event placeholder";

			events.put(root_event.eventId, root_event);
			eventIdsByDepth.put(0, new HashSet<>());
			eventIdsByDepth.get(0).add(root_event.eventId);
		}
	}

	public class Event {
		// We only support sending messages currently, so an event is essentially a message.
		String eventId;
		int depth;
		String senderId;
		HashSet<String> parentEventIds;
		Timestamp timestamp;
		String content;

		public Event(MessageContext message){
			eventId = message.getMessageId();
			depth = message.getDepth();
			parentEventIds = new HashSet<>(message.getParentMessageId());
			timestamp = message.getTimestamp();
			content = message.getMessageContent();
			senderId = message.getOwnerId();
		}

		public Event() {}
	}
	// End DataStructures Only for internal use of this the EventHandler.
	// -----------------------------------------------------------------------------------------------------------------

	// Static helper functions below.
    public static HashMap<String, List<MessageContext>> SortMessagesByRoomId(List<MessageContext> messages) {
        HashMap<String, List<MessageContext>> messagesByRoomId = new HashMap<>();
        for (MessageContext msg: messages) {
            String roomId = msg.getRoomId();
            if (messagesByRoomId.containsKey(roomId)) {
                messagesByRoomId.get(roomId).add(msg);
            } else {
                messagesByRoomId.put(roomId, new LinkedList<>(Collections.singleton(msg)));
            }
        }
        return messagesByRoomId;
    }

	public static String GenerateEventId(Timestamp timestamp, String userId, String content, int depth) {
		return timestamp.toString()+userId;
	}
}
