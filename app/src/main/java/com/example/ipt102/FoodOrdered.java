package com.example.ipt102;

public class FoodOrdered {

    String foodName, foodImage;
    double foodPrice;
    int foodQuantity, foodOrderID;

    public FoodOrdered(int foodOrderID ,String foodImage, String foodName, double foodPrice,  int foodQuantity) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
        this.foodQuantity = foodQuantity;
        this.foodOrderID = foodOrderID;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public int getFoodQuantity(){
        return foodQuantity;
    }

    public int getFoodOrderID() {
        return foodOrderID;
    }
}
