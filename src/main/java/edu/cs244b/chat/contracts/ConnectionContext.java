package edu.cs244b.chat.contracts;

public class ConnectionContext implements Comparable<ConnectionContext>{

    String roomId;
    String roomName;
    String hostName;
    int port;

    public ConnectionContext(String roomName, String hostName, int port) {
        this.roomName = roomName;
        this.hostName = hostName;
        this.port = port;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int compareTo(ConnectionContext o) {
        String addr1 = hostName + port;
        String addr2 = o.getHostName() + o.getPort();
        return addr1.compareTo(addr2);
    }
}
