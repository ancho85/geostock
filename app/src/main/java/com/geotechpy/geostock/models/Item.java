package com.geotechpy.geostock.models;

/**
 * Created by ancho on 27/06/15.
 */
public class Item {

    private String code;
    private String name;
    private String type;

    public Item(String code, String name, String type) {
        setCode(code);
        setName(name);
        setType(type);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
