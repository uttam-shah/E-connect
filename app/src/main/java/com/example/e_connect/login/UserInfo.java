package com.example.e_connect.login;

public class UserInfo {
    String dob, name, pass, phone, userName;

    public UserInfo() {

    }

    public UserInfo(String dob, String name, String pass, String phone, String userName) {
        this.dob = dob;
        this.name = name;
        this.pass = pass;
        this.phone = phone;
        this.userName = userName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
