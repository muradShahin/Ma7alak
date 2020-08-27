package com.muradit.projectx.Model;

import com.muradit.projectx.Model.others.CurrentStoreChat;
import com.muradit.projectx.Model.others.CurrentUserChat;

public class ChatRoom {

    private String customerId;
    private String storeId;
    private String roomId;
    private CurrentStoreChat storeChat;
    private CurrentUserChat userChat;

    public CurrentStoreChat getStoreChat() {
        return storeChat;
    }

    public void setStoreChat(CurrentStoreChat storeChat) {
        this.storeChat = storeChat;
    }

    public CurrentUserChat getUserChat() {
        return userChat;
    }

    public void setUserChat(CurrentUserChat userChat) {
        this.userChat = userChat;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }


}
