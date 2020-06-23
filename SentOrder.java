package com.example.convykitchen;

class SentOrder {
    private String MenuCategory,MenuDescription,MenuImageUri,MenuPrice,date,items,
            menuName,totalPrice,ResKey,userName,FoodKey,userID,profileImage;

    public SentOrder() {

    }

    public SentOrder(String menuCategory, String menuDescription, String menuImageUri, String menuPrice, String date, String items, String menuName, String totalPrice, String resKey, String userName, String foodKey, String userID, String profileImage) {
        MenuCategory = menuCategory;
        MenuDescription = menuDescription;
        MenuImageUri = menuImageUri;
        MenuPrice = menuPrice;
        this.date = date;
        this.items = items;
        this.menuName = menuName;
        this.totalPrice = totalPrice;
        ResKey = resKey;
        this.userName = userName;
        FoodKey = foodKey;
        this.userID = userID;
        this.profileImage = profileImage;
    }

    public String getMenuCategory() {
        return MenuCategory;
    }

    public void setMenuCategory(String menuCategory) {
        MenuCategory = menuCategory;
    }

    public String getMenuDescription() {
        return MenuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        MenuDescription = menuDescription;
    }

    public String getMenuImageUri() {
        return MenuImageUri;
    }

    public void setMenuImageUri(String menuImageUri) {
        MenuImageUri = menuImageUri;
    }

    public String getMenuPrice() {
        return MenuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        MenuPrice = menuPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getResKey() {
        return ResKey;
    }

    public void setResKey(String resKey) {
        ResKey = resKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFoodKey() {
        return FoodKey;
    }

    public void setFoodKey(String foodKey) {
        FoodKey = foodKey;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
