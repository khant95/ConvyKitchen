package com.example.convykitchen;

class Restaurent {
    private String city,country,logoImageUrl,restuarentName,streetAddress,username;

    public Restaurent() {
    }

    public Restaurent(String city, String country, String logoImageUrl, String restuarentName, String streetAddress, String username) {
        this.city = city;
        this.country = country;
        this.logoImageUrl = logoImageUrl;
        this.restuarentName = restuarentName;
        this.streetAddress = streetAddress;
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLogoImageUrl() {
        return logoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }

    public String getRestuarentName() {
        return restuarentName;
    }

    public void setRestuarentName(String restuarentName) {
        this.restuarentName = restuarentName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
