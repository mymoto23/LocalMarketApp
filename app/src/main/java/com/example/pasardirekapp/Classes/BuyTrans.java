package com.example.pasardirekapp.Classes;

public class BuyTrans extends Transaction {
    private Supplier supplier;

    public BuyTrans() {

    }

    public BuyTrans(long date, int amount, int quantity, Product product, Supplier supplier) {
        this.date = date;
        this.amount = amount;
        this.quantity = quantity;
        this.product = product;
        this.supplier = supplier;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
