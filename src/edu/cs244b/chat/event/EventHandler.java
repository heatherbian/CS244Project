package edu.cs244b.chat.event;

import java.sql.Timestamp;
import java.util.*;

import edu.cs244b.chat.contracts.IEventHandler;
import edu.cs244b.chat.contracts.MessageContext;
import jdk.internal.org.objectweb.asm.Handle;

public class EventHandler implements IEventHandler {
    // Data structure definitions
	public class Room {
		public String roomId;
		public ArrayList<String> userIds;
		public ArrayList<Server> servers;
		public EventGraph eventGraph;
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

    // Implements the interface
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
		List<MessageContext> messagesToSend = new ArrayList<>();
		for (MessageContext msg : messageContext) {
			messagesToSend.addAll(HandleNewMessage(msg));
		}
		return messagesToSend;
	}

    public List<MessageContext> ConvertAllRoomsToMessageContexts() {
        LinkedList<MessageContext> messageContexts = new LinkedList<>();
        for (String roomId : rooms.keySet()) {
            Room room = rooms.get(roomId);
            for (Event event: room.eventGraph.events.values()) {
                messageContexts.add(new MessageContext(roomId, new ArrayList<>(room.userIds), event.senderId, new ArrayList<>(event.parentEventIds),
                        event.eventId, event.timestamp, event.depth, event.content));
            }
        }
        return messageContexts;
    }

	// The new_message argument is supposed to have the timestamp, owner_id, message content and the room id. Maybe
	// message ID? (generated in event handler vs gui)
	public List<MessageContext> HandleNewMessage(MessageContext new_message) {
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
		return Arrays.asList(new_message);
	}

    public HashMap<String, Room> rooms;

    // Assume the messages argument is not null.
    public EventHandler(List<MessageContext> messages) {
        rooms = new HashMap<>();
        HashMap<String, List<MessageContext>> messagesByRoomId = SortMessagesByRoomId(messages);
        for (String roomId: messagesByRoomId.keySet()) {
            List<MessageContext> msgs = messagesByRoomId.get(roomId);
            Room room = new Room();
            room.roomId = roomId;
            room.eventGraph = new EventGraph(roomId);
            room.userIds = msgs.size() != 0 ? new ArrayList<>(msgs.get(0).getUserIds()) : new ArrayList<>();
            AddMessagesToEventGraph(room.eventGraph, messages);
        }
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
