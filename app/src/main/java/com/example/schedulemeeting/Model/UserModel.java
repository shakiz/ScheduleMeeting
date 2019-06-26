package com.example.schedulemeeting.Model;

public class UserModel {
    private int icon;
    private String name;

    public UserModel(int icon,String name){
        this.icon=icon;
        this.name=name;
    }

    public UserModel(){

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
