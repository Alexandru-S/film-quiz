package com.glassbyte.film_quiz2;

/**
 * Created by Alex on 28/11/15.
 */

/**My SQL list class intended to define
 * a list of the parameters used
 * in the sql database
 *
 */
public class MySqlDBList {
    String name;
    String price;
    String photoId;

    public MySqlDBList(String name, String price, String photoId) {
        this.name = name;
        this.price = price;
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

}