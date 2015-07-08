package com.geotechpy.geostock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.geotechpy.geostock.models.StockDetail;

import java.util.ArrayList;

/**
 * Manages connections to table StockDetail
 */
public class StockDetailManager {
    public static final String TABLE_NAME = "StockDetail";
    public static final String CN_STOCKSERNR = "stock_sernr";
    public static final String CN_LINENR = "linenr";
    public static final String CN_ITEMCODE = "item_code";
    public static final String CN_QTY = "qty";

    private SQLiteDatabase db;


    public StockDetailManager(Context ctx){
        DBHelper helper = DBHelper.getInstance(ctx);
        db = helper.getWritableDatabase();
    }

    private ContentValues setContentValues(Integer stock_sernr, Integer linenr, String item_code, Float qty) {
        ContentValues values = new ContentValues();
        values.put(CN_STOCKSERNR, stock_sernr);
        values.put(CN_LINENR, linenr);
        values.put(CN_ITEMCODE, item_code);
        values.put(CN_QTY, qty);
        return values;
    }

    public void insert(Integer stock_sernr, Integer linenr, String item_code, Float qty) {
        db.insert(TABLE_NAME, null, setContentValues(stock_sernr, linenr, item_code, qty));
    }

    public void delete(Integer stock_sernr, Integer linenr) {
        db.delete(TABLE_NAME,
                CN_STOCKSERNR + "=? AND " + CN_LINENR + "=?",
                new String[]{stock_sernr.toString(), linenr.toString()});
    }

    public void update(Integer stock_sernr, Integer linenr, String item_code, Float qty) {
        db.update(TABLE_NAME,
                setContentValues(stock_sernr, linenr, item_code, qty),
                CN_STOCKSERNR + "=? AND " + CN_LINENR + "=?",
                new String[]{stock_sernr.toString(), linenr.toString()});
    }

    public ArrayList<StockDetail> getStockDetails() {
        String[] columns = new String[]{CN_STOCKSERNR, CN_LINENR, CN_ITEMCODE, CN_QTY};
        Cursor c = null;
        ArrayList<StockDetail> alstockDetails = new ArrayList<>();
        try{
            c = db.query(TABLE_NAME, columns, null, null, null, null, null);
            while (c.moveToNext()){
                Integer position = c.getPosition();
                StockDetail stockDetail= new StockDetail(Integer.valueOf(c.getString(0)), Integer.valueOf(c.getString(1)), c.getString(2), Float.valueOf(c.getString(3)));
                alstockDetails.add(position, stockDetail);
            }
        }finally {
            if (c != null){
                c.close();
            }
        }
        return alstockDetails;
    }

    public Integer count() {
        Integer count = 0;
        Cursor c = db.rawQuery("SELECT COUNT(*) AS count FROM " + TABLE_NAME, null);
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }

    public static StockDetail load(Context ctx, Integer stock_sernr, Integer linenr) {
        DBHelper sdb = DBHelper.getInstance(ctx);
        SQLiteDatabase db = null;
        StockDetail stockDetail = new StockDetail();
        try{
            db = sdb.getReadableDatabase();
            String[] columns = new String[]{CN_STOCKSERNR, CN_LINENR, CN_ITEMCODE, CN_QTY};
            String where = "stock_sernr = '" + stock_sernr.toString() + "'" +
                    " AND linenr = '" + linenr.toString() + "'";
            Cursor c = null;
            try{
                c = db.query(TABLE_NAME, columns, where, null, null, null, null);
                if (c.moveToFirst()) {
                    stockDetail.setStock_sernr(Integer.valueOf(c.getString(0)));
                    stockDetail.setLinenr(Integer.valueOf(c.getString(1)));
                    stockDetail.setItem_code(c.getString(2));
                    stockDetail.setQty(Float.valueOf(c.getString(3)));
                }
            }finally {
                if (c != null){
                    c.close();
                }
            }
        }finally {
            if (db != null){
                db.close();
            }
        }
        return stockDetail;
    }
}
