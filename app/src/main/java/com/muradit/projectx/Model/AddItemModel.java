package com.muradit.projectx.Model;

public class AddItemModel {
    private String item_name;
    private String item_desc;
    private String item_price;
    private String item_category;
    private String item_store;
    private String store_email;
    private String item_image;
    private String item_state;
    private String item_id;
    private String store_id;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public AddItemModel(String item_name, String item_desc, String item_price, String item_category, String item_store, String store_email, String item_image
    , String item_state) {
        this.item_name = item_name;
        this.item_desc = item_desc;
        this.item_price = item_price;
        this.item_category = item_category;
        this.item_store = item_store;
        this.store_email = store_email;
        this.item_image=item_image;
        this.item_state=item_state;
    }

    public String getItem_state() {
        return item_state;
    }

    public void setItem_state(String item_state) {
        this.item_state = item_state;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    public String getItem_store() {
        return item_store;
    }

    public void setItem_store(String item_store) {
        this.item_store = item_store;
    }

    public String getStore_email() {
        return store_email;
    }

    public void setStore_email(String store_email) {
        this.store_email = store_email;
    }
}
