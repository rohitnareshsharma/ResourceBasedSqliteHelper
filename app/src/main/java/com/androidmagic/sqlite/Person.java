package com.androidmagic.sqlite;

public class Person {

    public Long id;
    
    public String name;
    
    public String address;
    
    @Override
    public String toString() {
        return "[ id: " + id + ", name: " + name + ", address: " + address + "]";
    }
    
}
