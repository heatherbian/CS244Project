package edu.cs244b.chat.utils;

import com.google.gson.Gson;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.reflect.TypeToken;
import edu.cs244b.chat.contracts.ConnectionContext;
import edu.cs244b.chat.contracts.MessageContext;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {

    static String MESSAGE_PATH = "message.json";
    static String CONNECTION_PATH = "connection.json";

    public static List<MessageContext> readMessage(String userId) {
        List<MessageContext> messages = null;
        try (Reader reader = Files.newBufferedReader(Paths.get(userId+"_"+MESSAGE_PATH))) {
            Gson gson = new Gson();
            messages = Arrays.asList(gson.fromJson(reader, MessageContext[].class));
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public static void writeMessage(String userId, List<MessageContext> list) {

        try (PrintWriter out = new PrintWriter(new FileWriter(userId+"_"+MESSAGE_PATH))) {
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
        try (Reader reader = Files.newBufferedReader(Paths.get(userId+"_"+CONNECTION_PATH))) {
            Gson gson = new Gson();
            connections = Arrays.asList(gson.fromJson(reader, ConnectionContext[].class));
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return connections;
    }

    public static void writeConnection(String userId, List<ConnectionContext> list) {
        try (PrintWriter out = new PrintWriter(new FileWriter(userId+"_"+CONNECTION_PATH))) {
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
}
