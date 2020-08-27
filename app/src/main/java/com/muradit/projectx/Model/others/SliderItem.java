package com.muradit.projectx.Model.others;

public class SliderItem {
    private String description;
    private String imageUri;

    public SliderItem(String description, String imageUri) {
        this.description = description;
        this.imageUri = imageUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
