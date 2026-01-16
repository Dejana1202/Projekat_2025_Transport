package com.example.projekat.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Bill implements Serializable {
    private static Integer billId;
    private Integer id;
    private List<String> relations;
    private LocalDateTime purchaseTime;
    private int price;
    public Bill(){
        super();
    }

    public Bill(List<String> relations, LocalDateTime purchaseTime, int price) {
        this.relations = relations;
        this.purchaseTime = purchaseTime;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static Integer getBillId() {
        return billId;
    }

    public static void setBillId(Integer billId) {
        Bill.billId = billId;
    }

    public List<String> getRelations() {
        return relations;
    }

    public void setRelations(List<String> relations) {
        this.relations = relations;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "relations=" + relations +
                ", purchaseTime=" + purchaseTime +
                ", price=" + price +
                '}';
    }
}
