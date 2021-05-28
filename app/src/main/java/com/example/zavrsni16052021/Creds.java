package com.example.zavrsni16052021;

public class Creds {
    private String username;
    private String password;

    public Creds(String mU, String mP){
        username=mU;
        password=mP;
    }
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isCorrect(String a, String b){
        if(a.equalsIgnoreCase(username) && b.equalsIgnoreCase(password)) return true;
        return false;
    }
}
