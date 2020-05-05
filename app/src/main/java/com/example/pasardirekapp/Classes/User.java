package com.example.pasardirekapp.Classes;

public class User {
    private String name;
    private String email;
    private String store;
    private int balance;
    private int init_balance;
    private int profit;
    private int revenue;

    public User(String name, String email, String store, int balance) {
        this.name = name;
        this.email = email;
        this.store = store;
        this.balance = balance;
        init_balance = balance;
        profit = 0;
        revenue = 0;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getInit_balance() {
        return init_balance;
    }

    public void setInit_balance(int init_balance) {
        this.init_balance = init_balance;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }
}
