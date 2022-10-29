package com.example.fwmda.Model;

import android.app.ProgressDialog;
import android.widget.Button;

public class Donations {

    String foodItemName, description, pickUpTime, pickUpDate, pickUpLocation, foodurl;
    public Donations(){};

    public Donations(String foodItemName,String description,String pickUpTime,String pickUpDate,
                     String pickUpLocation,String foodurl){
        this.foodItemName = foodItemName;
        this.description = description;
        this.pickUpTime= pickUpTime;
        this.pickUpDate=pickUpDate;
        this.pickUpLocation = pickUpLocation;
        this.foodurl=foodurl;
    }
    ProgressDialog progressDialog;

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public void setFoodurl(String foodurl) {
        this.foodurl = foodurl;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public String getDescription() {
        return description;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public String getFoodurl() {
        return foodurl;
    }
}


