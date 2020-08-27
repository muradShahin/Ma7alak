package com.muradit.projectx.Model;

public class Follow {
    private String userId;
    private String storeId;
    private String relKey;

    public String getRelKey() {
        return relKey;
    }

    public void setRelKey(String relKey) {
        this.relKey = relKey;
    }

    public Follow(String userId, String storeId) {
        this.userId = userId;
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
