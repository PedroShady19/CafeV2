package com.productions.esaf.cafe.Model;

public class Order {
    private String foodID;
    private String foodName;
    private String Quantity;
    private String foodPrice;
    private String Discount;

    public Order() {
    }

    public Order(String foodID, String foodName, String quantity, String foodPrice, String discount) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.Quantity = quantity;
        this.foodPrice = foodPrice;
        this.Discount = discount;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
