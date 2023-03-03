package com.example.ipt102;

public class Foods {

    String foodName, foodImage;
    double foodPrice;
    int foodID;

    public Foods(String foodImage, String foodName, double foodPrice, int foodID) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
        this.foodID = foodID;
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

    public int getFoodID() {
        return foodID;
    }
}
