package com.vedmitryapps.mymap.model;


import io.realm.RealmObject;

public class MarkerImage extends RealmObject {

    private int id;
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
