package com.example.pasardirekapp.Classes;

public class SellTrans extends Transaction {
    private Customer customer;

    public SellTrans() {

    }

    public SellTrans(long date, int amount, int quantity, Product product, Customer customer) {
        this.date = date;
        this.amount = amount;
        this.quantity = quantity;
        this.product = product;
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}


