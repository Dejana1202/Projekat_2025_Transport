package com.example.projekat.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Reprezentacija racuna, koji se kreira prilikom kupovine karte za prevoz.
 */
public class Bill implements Serializable {
    private static Integer billId;
    private Integer id;
    /**
     * String reprezentacija svih stanica, kroz koje prolazi data putanja prevoza.
     */
    private List<String> relations;
    /**
     * Vrijeme kupovine karte tj. vrijeme kreiranja samog racuna.
     */
    private LocalDateTime purchaseTime;
    /**
     * Cijena racuna tj. suma svih cijena, svakog pojedinacnog polaska na datim relacijama.
     */
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

    /**
     * @return String reprezentacija racuna, nastalog pri kupovini karte za prevoz.
     */
    @Override
    public String toString() {
        return "Bill{" +
                "relations=" + relations +
                ", purchaseTime=" + purchaseTime +
                ", price=" + price +
                '}';
    }
}
