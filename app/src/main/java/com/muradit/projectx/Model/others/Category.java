package com.muradit.projectx.Model.others;

public class Category {
    private String Name;
    public static boolean Flag=false;

    public Category(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
