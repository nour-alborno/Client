package com.example.client.Model;

public class CliantsNumbers {
    String name ;
    String id;
    int mobile;

    public CliantsNumbers() {
    }

    public CliantsNumbers(String name, String id, int mobile) {
        this.name = name;
        this.id = id;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }
}
