package com.geotechpy.geostock.models;

/**
 * Created by ancho on 27/06/15.
 */
public class Stock {

    private Integer sernr;
    private String type;
    private String status;
    private String user_code;
    private Integer zone_sernr;

    public Stock(Integer sernr, String type, String status, String user_code, Integer zone_sernr) {

        setSernr(sernr);
        setType(type);
        setStatus(status);
        setUser_code(user_code);
        setZone_sernr(zone_sernr);
    }

    public Integer getSernr() {
        return sernr;
    }

    public void setSernr(Integer sernr) {
        this.sernr = sernr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public Integer getZone_sernr() {
        return zone_sernr;
    }

    public void setZone_sernr(Integer zone_sernr) {
        this.zone_sernr = zone_sernr;
    }
}
