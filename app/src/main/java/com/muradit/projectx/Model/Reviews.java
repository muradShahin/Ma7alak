package com.muradit.projectx.Model;

public class Reviews {
    private String userId;
    private String userName;
    private String review;
    private String date;
    private String storeId;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Reviews(String userId, String userName, String review, String date,String storeId) {
        this.userId = userId;
        this.userName = userName;
        this.review = review;
        this.date = date;
        this.storeId=storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
