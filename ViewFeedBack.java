package com.example.convykitchen;

class ViewFeedBack {
    String FoodKey,MenuPrice,ResKey,date,fullName,menuName,profileImage,feedback;

    public ViewFeedBack() {
    }

    public ViewFeedBack(String foodKey, String menuPrice, String resKey, String date, String fullName, String menuName, String profileImage, String feedback) {
        FoodKey = foodKey;
        MenuPrice = menuPrice;
        ResKey = resKey;
        this.date = date;
        this.fullName = fullName;
        this.menuName = menuName;
        this.profileImage = profileImage;
        this.feedback = feedback;
    }

    public String getFoodKey() {
        return FoodKey;
    }

    public void setFoodKey(String foodKey) {
        FoodKey = foodKey;
    }

    public String getMenuPrice() {
        return MenuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        MenuPrice = menuPrice;
    }

    public String getResKey() {
        return ResKey;
    }

    public void setResKey(String resKey) {
        ResKey = resKey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
