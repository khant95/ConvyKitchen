package com.example.convykitchen;

class Food {

    private String MenuCategory,MenuDescription,MenuImageUri,MenuPrice,menuName;

    public Food() {
    }

    public Food(String menuCategory, String menuDescription, String menuImageUri, String menuPrice, String menuName) {
        MenuCategory = menuCategory;
        MenuDescription = menuDescription;
        MenuImageUri = menuImageUri;
        MenuPrice = menuPrice;
        this.menuName = menuName;
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

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
