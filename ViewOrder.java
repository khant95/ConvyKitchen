package com.example.convykitchen;

class ViewOrder {
    String FoodKey;
    String MenuCategory;
    String MenuDescription;
    String MenuImageUri;
    String MenuPrice;
    String ResKey;
    String date;
    String items;
    String menuName;
    String profileImage;
    String totalPrice;
    String userID;
    String userName;

    public ViewOrder(String foodKey, String menuCategory, String menuDescription, String menuImageUri, String menuPrice, String resKey, String date, String items, String menuName, String profileImage, String totalPrice, String userID, String userName, String customerAddress) {
        FoodKey = foodKey;
        MenuCategory = menuCategory;
        MenuDescription = menuDescription;
        MenuImageUri = menuImageUri;
        MenuPrice = menuPrice;
        ResKey = resKey;
        this.date = date;
        this.items = items;
        this.menuName = menuName;
        this.profileImage = profileImage;
        this.totalPrice = totalPrice;
        this.userID = userID;
        this.userName = userName;
        this.customerAddress = customerAddress;
    }

    String customerAddress;

    public ViewOrder() {
    }

    public String getFoodKey() {
        return FoodKey;
    }

    public void setFoodKey(String foodKey) {
        FoodKey = foodKey;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}
