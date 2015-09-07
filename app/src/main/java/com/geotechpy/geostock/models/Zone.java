package com.geotechpy.geostock.models;

/**
 * Created by ancho on 27/06/15.
 */
public class Zone {

    private Integer sernr;
    private String name;
    private String type;
    private String depo_name;

    public Zone() {
        setSernr(0);
        setName("");
        setType("");
        setDepo_name("");
    }

    public Zone(Integer sernr, String name, String type, String depo_name) {
        setSernr(sernr);
        setName(name);
        setType(type);
        setDepo_name(depo_name);
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

    public String getDepo_name() {
        return depo_name;
    }

    public void setDepo_name(String depo_name) {
        this.depo_name = depo_name;
    }
}
