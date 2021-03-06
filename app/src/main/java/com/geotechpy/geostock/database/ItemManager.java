package com.geotechpy.geostock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.geotechpy.geostock.models.Item;

import java.util.ArrayList;

/**
 * Manages connections to table Item
 */
public class ItemManager {
    public static final String TABLE_NAME = "Item";
    public static final String CN_CODE = "code";
    public static final String CN_NAME = "name";
    public static final String CN_TYPE = "type";
    public static final String CN_BARCODE = "barcode";

    private SQLiteDatabase db;


    public ItemManager(Context ctx){
        DBHelper helper = DBHelper.getInstance(ctx);
        db = helper.getWritableDatabase();
    }

    private ContentValues setContentValues(String code, String name, Long barCode, String type) {
        ContentValues values = new ContentValues();
        values.put(CN_CODE, code);
        values.put(CN_NAME, name);
        values.put(CN_BARCODE, barCode);
        values.put(CN_TYPE, type);
        return values;
    }

    public void insert(String code, String name, Long barCode, String type) {
        db.insert(TABLE_NAME, null, setContentValues(code, name, barCode, type));
    }

    public void delete(String code) {
        db.delete(TABLE_NAME, CN_CODE + "=?", new String[]{code});
    }

    public void update(String code, String name, Long barCode, String type) {
        db.update(TABLE_NAME, setContentValues(code, name, barCode, type), CN_CODE + "=?", new String[]{code});
    }

    public ArrayList<Item> getItems() {
        String[] columns = new String[]{CN_CODE, CN_NAME, CN_BARCODE, CN_TYPE};
        Cursor c = null;
        ArrayList<Item> alitem = new ArrayList<>();
        try{
            c = db.query(TABLE_NAME, columns, null, null, null, null, null);
            while (c.moveToNext()){
                Integer position = c.getPosition();
                Item item = new Item(c.getString(0), c.getString(1), Long.valueOf(c.getString(2)), c.getString(3));
                alitem.add(position, item);
            }
        }finally {
            if (c != null){
                c.close();
            }
        }
        return alitem;
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

    public static Item load(Context ctx, String code) {
        DBHelper sdb = DBHelper.getInstance(ctx);
        SQLiteDatabase db = null;
        Item item = new Item();
        try{
            db = sdb.getReadableDatabase();
            String[] columns = new String[]{CN_CODE, CN_NAME, CN_BARCODE, CN_TYPE};
            String where = "code = '" + code + "'";
            Cursor c = null;
            try{
                c = db.query(TABLE_NAME, columns, where, null, null, null, null);
                if (c.moveToFirst()) {
                    item.setCode(c.getString(0));
                    item.setName(c.getString(1));
                    item.setBarcode(Long.valueOf(c.getString(2)));
                    item.setType(c.getString(3));
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
        return item;
    }
}

