package com.productions.esaf.cafe.Model;

public class Utilizador {
    private String name;
    private String password;
    private String phone;
    private String IsStaff;

    public Utilizador() {
    }

    public Utilizador(String name, String password) {
        this.name = name;
        this.password = password;
        this.IsStaff ="false";

    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
