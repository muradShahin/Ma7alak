package com.muradit.projectx.Model.others;

public class MessageNeeds {
    private String roomId;
    private boolean exist=false;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
