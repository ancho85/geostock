package com.geotechpy.geostock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.geotechpy.geostock.models.Zone;

import java.util.ArrayList;

/**
 * Manages connections to table Zone
 */
public class ZoneManager {
    public static final String TABLE_NAME = "Zone";
    public static final String CN_SERNR = "sernr";
    public static final String CN_NAME = "name";
    public static final String CN_TYPE = "type";
    public static final String CN_DEPO_NAME = "depo_name";

    private SQLiteDatabase db;


    public ZoneManager(Context ctx){
        DBHelper helper = DBHelper.getInstance(ctx);
        db = helper.getWritableDatabase();
    }

    private ContentValues setContentValues(Integer sernr, String name, String type, String depo_name) {
        ContentValues values = new ContentValues();
        values.put(CN_SERNR, sernr);
        values.put(CN_NAME, name);
        values.put(CN_TYPE, type);
        values.put(CN_DEPO_NAME, depo_name);
        return values;
    }

    public void insert(Integer sernr, String name, String type, String depo_name) {
        db.insert(TABLE_NAME, null, setContentValues(sernr, name, type, depo_name));
    }

    public void delete(Integer sernr) {
        db.delete(TABLE_NAME, CN_SERNR + "=?", new String[]{sernr.toString()});
    }

    public void update(Integer sernr, String name, String type, String depo_name) {
        db.update(TABLE_NAME, setContentValues(sernr, name, type, depo_name), CN_SERNR + "=?", new String[]{sernr.toString()});
    }

    public ArrayList<Zone> getZones(String type) {
        String[] columns = new String[]{CN_SERNR, CN_NAME, CN_TYPE, CN_DEPO_NAME};
        String where = "";
        if (!TextUtils.isEmpty(type)){
            where = CN_TYPE + "= '" + type + "'";
        }
        Cursor c = null;
        ArrayList<Zone> alzone = new ArrayList<>();
        try{
            c = db.query(TABLE_NAME, columns, where, null, null, null, null);
            while (c.moveToNext()){
                Integer position = c.getPosition();
                Zone zone = new Zone(Integer.valueOf(c.getString(0)), c.getString(1), c.getString(2), c.getString(3));
                alzone.add(position, zone);
            }
        }finally {
            if (c != null){
                c.close();
            }
        }
        return alzone;
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

    public static Zone load(Context ctx, Integer sernr) {
        DBHelper sdb = DBHelper.getInstance(ctx);
        SQLiteDatabase db = null;
        Zone zone = new Zone();
        try{
            db = sdb.getReadableDatabase();
            String[] columns = new String[]{CN_SERNR, CN_NAME, CN_TYPE, CN_DEPO_NAME};
            String where = "sernr = '" + sernr.toString() + "'";
            Cursor c = null;
            try{
                c = db.query(TABLE_NAME, columns, where, null, null, null, null);
                if (c.moveToFirst()) {
                    zone.setSernr(Integer.valueOf(c.getString(0)));
                    zone.setName(c.getString(1));
                    zone.setType(c.getString(2));
                    zone.setDepo_name(c.getString(3));
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
        return zone;
    }
}

