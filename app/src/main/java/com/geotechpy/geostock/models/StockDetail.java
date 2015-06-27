package com.geotechpy.geostock.models;

/**
 * Created by ancho on 27/06/15.
 */
public class StockDetail {

    private Integer stock_sernr;
    private Integer linenr;
    private String item_code;
    private Float qty;

    public StockDetail(Integer stock_sernr, Integer linenr, String item_code, Float qty) {
        setStock_sernr(stock_sernr);
        setLinenr(linenr);
        setItem_code(item_code);
        setQty(qty);
    }

    public Integer getStock_sernr() {
        return stock_sernr;
    }

    public void setStock_sernr(Integer stock_sernr) {
        this.stock_sernr = stock_sernr;
    }

    public Integer getLinenr() {
        return linenr;
    }

    public void setLinenr(Integer linenr) {
        this.linenr = linenr;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public Float getQty() {
        return qty;
    }

    public void setQty(Float qty) {
        this.qty = qty;
    }
}
