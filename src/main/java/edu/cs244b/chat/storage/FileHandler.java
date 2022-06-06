package edu.cs244b.chat.storage;

import com.google.gson.Gson;
import edu.cs244b.chat.model.ConnectionContext;
import edu.cs244b.chat.model.MessageContext;
import edu.cs244b.chat.model.RoomContext;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {

    static String ADDR_PREFIX = "src/main/resources/";
    static String MESSAGE_PATH = "message.json";
    static String CONNECTION_PATH = "connection.json";
    static String ROOM_PATH = "room.json";

    public static List<MessageContext> readMessage(String userId) {
        List<MessageContext> messages = null;
        try (Reader reader = Files.newBufferedReader(Paths.get(ADDR_PREFIX+userId+"_"+MESSAGE_PATH))) {
            Gson gson = new Gson();
            messages = Arrays.asList(gson.fromJson(reader, MessageContext[].class));
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public static void writeMessage(String userId, List<MessageContext> list) {

        try (PrintWriter out = new PrintWriter(new FileWriter(ADDR_PREFIX+userId+"_"+MESSAGE_PATH))) {
            Gson gson = new Gson();
            out.append("[");
            String messageContextStr = list.stream().map(messageContext -> {
                return gson.toJson(messageContext);
            }).collect(Collectors.joining(","));
            out.append(messageContextStr);
            out.append("]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<ConnectionContext> readConnection(String userId) {
        List<ConnectionContext> connections = null;
        try (Reader reader = Files.newBufferedReader(Paths.get(ADDR_PREFIX+userId+"_"+CONNECTION_PATH))) {
            Gson gson = new Gson();
            connections = Arrays.asList(gson.fromJson(reader, ConnectionContext[].class));
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return connections;
    }

    public static void writeConnection(String userId, List<ConnectionContext> list) {
        try (PrintWriter out = new PrintWriter(new FileWriter(ADDR_PREFIX+userId+"_"+CONNECTION_PATH))) {
            Gson gson = new Gson();
            out.append("[");
            String messageContextStr = list.stream().map(connectionContext -> {
                return gson.toJson(connectionContext);
            }).collect(Collectors.joining(","));
            out.append(messageContextStr);
            out.append("]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeRoom(String userId, List<RoomContext> rooms) {
        try (PrintWriter out = new PrintWriter(new FileWriter(ADDR_PREFIX+userId+"_"+ROOM_PATH))) {
            Gson gson = new Gson();
            out.append("[");
            String roomContextStr = rooms.stream().map(room -> {
                return gson.toJson(room);
            }).collect(Collectors.joining(","));
            out.append(roomContextStr);
            out.append("]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<RoomContext> readRoom(String userId) {
        List<RoomContext> connections = null;
        try (Reader reader = Files.newBufferedReader(Paths.get(ADDR_PREFIX+userId+"_"+ROOM_PATH))) {
            Gson gson = new Gson();
            connections = Arrays.asList(gson.fromJson(reader, RoomContext[].class));
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return connections;
    }

    public static void main(String[] args){
        List<RoomContext> rooms = new LinkedList<>();
        List<String> userIds = Arrays.asList("Jingyi", "Shaohui");
        RoomContext room = new RoomContext("314983684", "Shaohui", userIds);
        rooms.add(room);

        userIds = Arrays.asList("Henry", "Jingyi", "Shaohui");
        room = new RoomContext("314983682", "CS244B Project", userIds);
        rooms.add(room);

        userIds = Arrays.asList("Jingyi", "Henry");
        room = new RoomContext("314983685", "Henry", userIds);
        rooms.add(room);

        writeRoom("Jingyi", rooms);
        rooms = readRoom("Jingyi");
        System.out.println("test");
    }
}
