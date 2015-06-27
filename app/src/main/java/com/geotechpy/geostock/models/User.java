package com.geotechpy.geostock.models;

/**
 * Created by ancho on 27/06/15.
 */
public class User {

    private String code;
    private String password;
    private String type;

    public User(String code, String password, String type) {

        setCode(code);
        setPassword(password);
        setType(type);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
