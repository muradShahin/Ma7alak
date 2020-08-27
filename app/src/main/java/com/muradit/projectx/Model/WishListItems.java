package com.muradit.projectx.Model;

public class WishListItems {
    private String item_id;
    private String user_email;

    public WishListItems(String item_id, String user_email) {
        this.item_id = item_id;
        this.user_email = user_email;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
