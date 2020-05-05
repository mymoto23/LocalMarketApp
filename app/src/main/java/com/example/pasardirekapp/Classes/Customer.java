package com.example.pasardirekapp.Classes;

public class Customer extends Contact {
    private int grade;
    private int trans_no;

    public Customer() {
    }

    public Customer(String name, String phone, int grade, int trans_no) {
        this.name = name;
        this.phone = phone;
        this.grade = grade;
        this.trans_no = trans_no;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getTrans_no() {
        return trans_no;
    }

    public void setTrans_no(int trans_no) {
        this.trans_no = trans_no;
    }
    public void applyChanges(String name, String phone) {
        if (!name.isEmpty())
            this.name = name;
        if (!phone.isEmpty())
            this.phone = phone;
    }
}
