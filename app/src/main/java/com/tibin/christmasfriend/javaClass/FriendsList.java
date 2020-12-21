package com.tibin.christmasfriend.javaClass;

import java.util.ArrayList;
import java.util.List;

public class FriendsList {

    private List<String> names = new ArrayList<>();

    public FriendsList() {
        this.names.add("Sobin");
        this.names.add("Soju");
        this.names.add("Chichava");
        this.names.add("Akku");
        this.names.add("Shiya");
        this.names.add("Jinson");
        this.names.add("Anson");
        this.names.add("Tibin");
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        for(String name : names) {
            this.names.add(name);
        }
    }
}
