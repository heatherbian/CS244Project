package edu.cs244b.chat.event;

import java.sql.Timestamp;
import java.util.*;

import edu.cs244b.chat.contracts.IEventHandler;
import edu.cs244b.chat.contracts.MessageContext;

public class EventHandler implements IEventHandler {

	public class Room {
		public String roomId;
		public ArrayList<User> users;
		public ArrayList<Server> servers;
		public EventGraph eventGraph;
	}

	public class User {
		public String id;
		public String username;
	}

	public class Server {
		public String ipAddress;
		public int port;
	}

	public class EventGraph {
		public HashMap<String, Event> events; // event_id to event.
		public HashMap<Integer, HashSet<String>> eventIdsByDepth;
		public int maxDepth; // depth of the latest events in the event graph.

		public HashSet<String> unknownEventIds;

		public  HashMap<String, Event> pendingEvents;

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
	}

	public HashMap<String, Room> rooms;

	@Override
	public List<MessageContext> analyzeMessage(List<MessageContext> messageContext) {
		// ReceiveMessages
		HashMap<String, List<MessageContext>> messagesByRoomId = new HashMap<>();
		for (MessageContext message : messageContext) {
			String roomId = message.getRoomId();
			if (!messageContext.contains(roomId)) {
				messagesByRoomId.put(roomId, new ArrayList<>());
			}
			List<MessageContext> messages = messagesByRoomId.get(roomId);
			messages.add(message);
		}
		for (String roomId : messagesByRoomId.keySet()) {
			if (!rooms.containsKey(roomId)) { continue; }
			AddMessagesToEventGraph(rooms.get(roomId).eventGraph, messagesByRoomId.get(roomId));
		}
		return null;
	}

	@Override
	public List<MessageContext> syncMessage(List<MessageContext> messageContext) {
		// TODO Auto-generated method stub
		return null;
	}

	private void AddMessagesToEventGraph(EventGraph eventGraph, List<MessageContext> messages) {
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

}
