package edu.cs244b.chat.model;

import javax.swing.*;
import java.util.List;

/**
 * Represent a list of all rooms for displaying RoomList UI
 *
 */
public class RoomListModel extends AbstractListModel<String> {

    List<RoomContext> roomList;

    public RoomListModel(List<RoomContext> roomList){
        this.roomList = roomList;
    }

    @Override
    public int getSize() {
        return roomList.size();
    }

    @Override
    public String getElementAt(int index) {
        if(index<roomList.size()){
            return roomList.get(index).roomName;
        }else {
            return null;
        }
    }
}
