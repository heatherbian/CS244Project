package edu.cs244b.chat.model;

public class ConnectionContext implements Comparable<ConnectionContext>{

    String userId;
    String hostName;
    int port;

    public ConnectionContext(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int compareTo(ConnectionContext o) {
        String addr1 = hostName + port;
        String addr2 = o.getHostName() + o.getPort();
        return addr1.compareTo(addr2);
    }
}
