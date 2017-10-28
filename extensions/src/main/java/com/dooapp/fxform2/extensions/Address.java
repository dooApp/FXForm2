package com.dooapp.fxform2.extensions;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 27/10/2017
 * Time: 12:42
 */
public class Address {

    public Address(String street, String city, String postcode) {
        this.street = street;
        this.city = city;
        this.postcode = postcode;
    }

    private String street;

    private String city;

    private String postcode;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

}
