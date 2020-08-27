package com.muradit.projectx.Model;

public class StoreModel {
    private String image;
    private String name;
    private String email;
    private String phone;
    private String city;
    private String ownerName;
    private String ownerEmail;
    private String lat;
    private String lng;
    private String category;
    private String about;
    private String id;


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public StoreModel(String image, String name, String email, String phone, String city, String ownerName, String ownerEmail
    , String lat, String lng, String category, String about) {
        this.image = image;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.lat=lat;
        this.lng=lng;
        this.category=category;
        this.about=about;

    }

    public StoreModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
