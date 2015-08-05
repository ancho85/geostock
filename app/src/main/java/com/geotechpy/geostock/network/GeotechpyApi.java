package com.geotechpy.geostock.network;

/**
 * Created by carlos.gomez on 04/08/2015.
 */
public class GeotechpyApi {
    public static final String BASEURL = "http://geotechpy.com/inventario/ajax/";

    public static String getUsersURL(){
        return "usuarios/get_full_usuarios_rest.php";
    }

    public static String getItemsURL(){
        return "productos/get_full_productos_rest.php";
    }

    public static String getZonesURL(){
        return "zonas/get_full_zonas_rest.php";
    }

    public static String getStockSaveURL(){
        return "inventario/save_inventario.php";
    }
}
