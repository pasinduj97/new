package com.example.madproject;

public class CatModel {

    private String id;
    private String name;

    public CatModel() {
    }

    public CatModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getIds() {
        return id;
    }

    public void setIds(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
