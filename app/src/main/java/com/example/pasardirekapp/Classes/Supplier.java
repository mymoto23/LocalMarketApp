package com.example.pasardirekapp.Classes;

public class Supplier extends Contact {
    private String company_name;

    public Supplier() {

    }

    public Supplier(String name, String phone, String company_name) {
        this.name = name;
        this.phone = phone;
        this.company_name = company_name;
    }

    public String getCompanyName() {
        return company_name;
    }

    public void setCompanyName(String company_name) {
        this.company_name = company_name;
    }

    public void applyChanges(String name, String phone, String company_name) {
        if (!name.isEmpty())
            this.name = name;
        if (!phone.isEmpty())
            this.phone = phone;
        if (!company_name.isEmpty())
            setCompanyName(company_name);
    }
}
