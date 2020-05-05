package com.example.pasardirekapp.Classes;

public class Product {

    private String name, description, category;
    private int buy_price, sell_price, quantity;
    private long date;


    public Product() {
    }

    public Product(String name, long date, String description, int buy_price, int sell_price, int quantity, String category) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.buy_price = buy_price;
        this.sell_price = sell_price;
        this.quantity = quantity;
        this.category = category;
    }

    public void applyChanges(String name, String description, String price, String sell_price, String quantity, long date) {
        if (!name.isEmpty())
            setName(name);
        if (!description.isEmpty())
            setDescription(description);
        if (!price.isEmpty())
            setBuy_price(Integer.parseInt(price));
        if(!sell_price.isEmpty())
            setSell_price(Integer.parseInt(sell_price));
        if (!quantity.isEmpty())
            setQuantity(Integer.parseInt(quantity));
        if (date != 0)
            setDate(date);
    }

    public int getSell_price() {
        return sell_price;
    }

    public void setSell_price(int sell_price) {
        this.sell_price = sell_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(int buy_price) {
        this.buy_price = buy_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
