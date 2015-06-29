package com.geotechpy.geostock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.geotechpy.geostock.models.StockDetail;

import java.util.ArrayList;

/**
 * Created by ancho on 27/06/15.
 */
public class StockDetailManager {
    public static final String TABLE_NAME = "StockDetail";
    public static final String CN_STOCKSERNR = "stock_sernr";
    public static final String CN_LINENR = "linenr";
    public static final String CN_ITEMCODE = "item_code";
    public static final String CN_QTY = "qty";

    private DBHelper helper;
    private SQLiteDatabase db;


    public StockDetailManager(Context ctx){
        helper = DBHelper.getInstance(ctx);
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

    public void delete(Integer stock_sernr) {
        db.delete(TABLE_NAME, CN_STOCKSERNR + "=?", new String[]{stock_sernr.toString()});
    }

    public void update(Integer stock_sernr, Integer linenr, String item_code, Float qty) {
        db.update(TABLE_NAME,
                setContentValues(stock_sernr, linenr, item_code, qty),
                CN_STOCKSERNR + "=?", new String[]{stock_sernr.toString()});
    }

    public ArrayList<StockDetail> getStockDetails() {
        String[] columns = new String[]{CN_STOCKSERNR, CN_LINENR, CN_ITEMCODE, CN_QTY};
        Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<StockDetail> alstockDetails = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Integer position = c.getPosition();
                StockDetail stockDetail= new StockDetail(Integer.valueOf(c.getString(0)), Integer.valueOf(c.getString(1)), c.getString(2), Float.valueOf(c.getString(3)));
                alstockDetails.add(position, stockDetail);
            } while (c.moveToNext());
        }
        c.close();
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
        SQLiteDatabase db = sdb.getReadableDatabase();
        StockDetail stockDetail = new StockDetail();
        String[] columns = new String[]{CN_STOCKSERNR, CN_LINENR, CN_ITEMCODE, CN_QTY};
        String where = "stock_sernr = '" + stock_sernr.toString() + "'" +
                " AND linenr = '" + linenr.toString() + "'";
        Cursor c = db.query(TABLE_NAME, columns, where, null, null, null, null);
        if (c.moveToFirst()) {
            stockDetail.setStock_sernr(Integer.valueOf(c.getString(0)));
            stockDetail.setLinenr(Integer.valueOf(c.getString(1)));
            stockDetail.setItem_code(c.getString(2));
            stockDetail.setQty(Float.valueOf(c.getString(3)));
        }
        c.close();
        return stockDetail;
    }
}
