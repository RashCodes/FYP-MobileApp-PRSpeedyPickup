package com.example.prspeedypickup.models;

public class setdata {

    String title;
    String randomkey;
    String detail;
    String amount;
    String category;
    String quantity;
    String ran;
    String users;

    public void setName(String name) {
        this.title = name;
    }
    public void setUser(String user) {
        this.users = user;
    }
    public void setRan(String ran) {
        this.ran = ran;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public void setCategory(String cate) {
        this.category = cate;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public void setAct(String active) {
        this.detail = active;
    }
    public String getRandomkey() {
        return randomkey;
    }
    public String getUser() {
        return users;
    }
    public String getRan() {
        return ran;
    }
    public String getCategory() {
        return category;
    }
    public String getAct() {
        return detail;
    }
    public String getQuantity() {
        return quantity;
    }
    public String getAmount() {
        return amount;
    }

    public void setRandomkey(String randomkey) {
        this.randomkey = randomkey;
    }

    public String getName() {
        return title;
    }

    public setdata() { }

    public setdata(String name, String key, String act, String amount, String cat) {
        this.title = name;
        this.randomkey = key;
        this.detail = act;
        this.amount = amount;
        this.category = cat;
    }
    public setdata(String name, String key, String act, String amount, String cat,String quantity,String random,String user) {
        this.title = name;
        this.randomkey = key;
        this.quantity = quantity;
        this.users = user;
        this.detail = act;
        this.category = cat;
        this.ran = random;

        this.amount = amount;
    }
    public setdata(String amount) {

        this.amount = amount;
    }
}
