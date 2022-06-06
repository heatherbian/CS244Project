package edu.cs244b.chat.model;

import java.util.List;

public class RoomContext {
    String roomId;
    String roomName;
    List<String> userIds;

    public RoomContext(String roomId, String roomName, List<String> userIds) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.userIds = userIds;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj.getClass() != this.getClass())
            return false;

        final RoomContext other = (RoomContext) obj;
        if (roomId.equals(other.getRoomId()))
            return true;
        return false;
    }
}
