package com.geotechpy.geostock.models;

/**
 * Created by ancho on 27/06/15.
 */
public class Zone {

    private Integer sernr;
    private String name;
    private String type;

    public Zone(Integer sernr, String name, String type) {
        setSernr(sernr);
        setName(name);
        setType(type);
    }

    public Integer getSernr() {
        return sernr;
    }

    public void setSernr(Integer sernr) {
        this.sernr = sernr;
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
