package com.geotechpy.geostock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.geotechpy.geostock.models.Stock;

import java.util.ArrayList;

/**
 * Created by ancho on 27/06/15.
 */
public class StockManager {
    public static final String TABLE_NAME = "Stock";
    public static final String CN_SERNR = "sernr";
    public static final String CN_TYPE = "type";
    public static final String CN_STATUS = "status";
    public static final String CN_USERCODE = "user_code";
    public static final String CN_ZONESERNR = "zone_sernr";

    private DBHelper helper;
    private SQLiteDatabase db;


    public StockManager(Context ctx){
        helper = DBHelper.getInstance(ctx);
        db = helper.getWritableDatabase();
    }

    private ContentValues setContentValues(Integer sernr, String type, String status, String user_code, Integer zone_sernr) {
        ContentValues values = new ContentValues();
        values.put(CN_SERNR, sernr);
        values.put(CN_TYPE, type);
        values.put(CN_STATUS, status);
        values.put(CN_USERCODE, user_code);
        values.put(CN_ZONESERNR, zone_sernr);
        return values;
    }

    public void insert(Integer sernr, String type, String status, String user_code, Integer zone_sernr) {
        db.insert(TABLE_NAME, null, setContentValues(sernr, type, status, user_code, zone_sernr));
    }

    public void delete(Integer sernr) {
        db.delete(TABLE_NAME, CN_SERNR + "=?", new String[]{sernr.toString()});
    }

    public void update(Integer sernr, String type, String status, String user_code, Integer zone_sernr) {
        db.update(TABLE_NAME,
                setContentValues(sernr, type, status, user_code, zone_sernr), CN_SERNR + "=?",
                new String[]{sernr.toString()});
    }

    public ArrayList<Stock> getStocks() {
        String[] columns = new String[]{CN_SERNR, CN_TYPE, CN_STATUS, CN_USERCODE, CN_ZONESERNR};
        Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<Stock> alstock = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Integer position = c.getPosition();
                Stock stock = new Stock(Integer.valueOf(c.getString(0)), c.getString(1), c.getString(2), c.getString(3), Integer.valueOf(c.getString(4)));
                alstock.add(position, stock);
            } while (c.moveToNext());
        }
        c.close();
        return alstock;
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

    public static Stock load(Context ctx, Integer sernr) {
        DBHelper sdb = DBHelper.getInstance(ctx);
        SQLiteDatabase db = sdb.getReadableDatabase();
        Stock stock = new Stock();
        String[] columns = new String[]{CN_SERNR, CN_TYPE, CN_STATUS, CN_USERCODE, CN_ZONESERNR};
        String where = "sernr = '" + sernr.toString() + "'";
        Cursor c = db.query(TABLE_NAME, columns, where, null, null, null, null);
        if (c.moveToFirst()) {
            stock.setSernr(Integer.valueOf(c.getString(0)));
            stock.setType(c.getString(1));
            stock.setStatus(c.getString(2));
            stock.setUser_code(c.getString(3));
            stock.setZone_sernr(Integer.valueOf(c.getString(4)));
        }
        c.close();
        db.close();
        return stock;
    }
}

